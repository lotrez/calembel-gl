package com.molkky.molkky.service;

import com.molkky.molkky.domain.*;
import com.molkky.molkky.model.CourtModel;
import com.molkky.molkky.model.MatchModel;
import com.molkky.molkky.model.UserModel;
import com.molkky.molkky.model.UserTournamentRoleModel;
import com.molkky.molkky.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import type.PhaseType;
import type.SetTeamIndex;
import type.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    @Lazy
    private SimpleGameService simpleGameService;

    @Autowired
    @Lazy
    private  PoolService poolService;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    PhaseRepository phaseRepository;
    @Autowired
    @Lazy
    private SwissService swissService;
    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CourtService courtService;

    @Autowired
    @Lazy
    private KnockoutService knockoutService;

    public void giveRandomCourtToMatch(Match match){
        List<Court> availableCourts = courtRepository.findByTournamentAndAvailable(match.getRound().getPhase().getTournament(), true);
        if(availableCourts.isEmpty()) return;
        Court court = availableCourts.get(0);
        court.setAvailable(false);
        court = courtRepository.save(court);
        match.setCourt(court);
        matchRepository.save(match);
    }

    public SetTeamIndex getUserTeamIndex(MatchModel match, UserTournamentRoleModel user) {
        Match matchEntity = getMatchFromModel(match);
        List<Team> matchTeams = matchEntity.getTeams();
        for(UserTournamentRole u : matchTeams.get(0).getUserTournamentRoles()) {
            if(Objects.equals(u.getId(), user.getId())) {
                return SetTeamIndex.TEAM1;
            }
        }
        for(UserTournamentRole u : matchTeams.get(1).getUserTournamentRoles()) {
            if(Objects.equals(u.getId(), user.getId())) {
                return SetTeamIndex.TEAM2;
            }
        }
        return SetTeamIndex.ORGA;
    }

    public Team getOppositeTeam(MatchModel match, UserTournamentRoleModel user){
        Match matchEntity = getMatchFromModel(match);

        for(UserTournamentRole u : matchEntity.getTeams().get(0).getUserTournamentRoles()) {
            if(Objects.equals(u.getId(), user.getId())) {
                return matchEntity.getTeams().get(1);
            }
        }
        return matchEntity.getTeams().get(0);
    }

    public static MatchModel getMatchModelFromEntity(Match match) {
        MatchModel matchModel = new MatchModel();
        matchModel.setId(match.getId());
        matchModel.setFinished(match.getFinished());
        matchModel.setNbSets(match.getNbSets());
        return matchModel;
    }

    public Boolean isUserInMatch(MatchModel match, UserModel user) {
        Match matchEntity = getMatchFromModel(match);
        for(UserTournamentRole u : matchEntity.getTeams().get(0).getUserTournamentRoles()) {
            if(Objects.equals(u.getUser().getId(), user.getId())) {
                return true;
            }
        }
        for(UserTournamentRole u : matchEntity.getTeams().get(1).getUserTournamentRoles()) {
            if(Objects.equals(u.getUser().getId(), user.getId())) {
                return true;
            }
        }
        return false;
    }

    public static List<MatchModel> createMatchModels(List<Match> matches) {
        List<MatchModel> matchModels = new ArrayList<>();
        User user = new User();
        UserTournamentRole userTournamentRole = new UserTournamentRole();
        userTournamentRole.setUser(user);
        userTournamentRole.setRole(UserRole.STAFF);
        if (matches != null) {
            for (Match match : matches) {
                if(match.getId() != null) {
                    match.setStaff(user);
                    matchModels.add(getMatchModelFromEntity(match));
                }
            }
        }
        return matchModels;
    }

    public void setCourt(MatchModel matchModel, CourtModel courtModel){
        Match match = getMatchFromModel(matchModel);
        Court oldCourt = match.getCourt();
        if(oldCourt != null){
            oldCourt.setAvailable(true);
            courtRepository.save(oldCourt);
        }
        Court court = courtService.getCourtFromModel(courtModel);
        court.setAvailable(false);
        match.setCourt(court);
        court.getMatches().add(match);
        matchRepository.save(match);
        courtRepository.save(court);
    }

    Match getMatchFromModel(MatchModel matchModel) {
        return matchRepository.findById(matchModel.getId());
    }

    public void validateMatch(Match match){

        Round round = match.getRound();

        List<Match> matches = round.getMatches();
        boolean finished = true;

        for(Match m : matches){
            if( Boolean.FALSE.equals(m.getFinished())){
                finished = false;
            }
        }

        if(Boolean.TRUE.equals(finished)){
            PhaseType type = round.getType();

            round.setFinished(true);

           roundRepository.save(round);

            switch (type){
                case POOL:
                    poolService.validateRound(round);
                    break;
                case FINNISH:

                    break;
                case KNOCKOUT:
                    knockoutService.validateRound(round);
                    break;
                case SWISSPOOL:
                    swissService.validateRound(round);
                    break;
                case SIMPLEGAME:
                    simpleGameService.validateRound(round);
                    break;
                default:
                    break;

            }
        }

    }

    public Boolean isMatchFinished(Match match){
        for (Set set : match.getSets()){
            if (Boolean.FALSE.equals(set.getFinished())){
                return false;
            }
        }
        return true;
    }

}
