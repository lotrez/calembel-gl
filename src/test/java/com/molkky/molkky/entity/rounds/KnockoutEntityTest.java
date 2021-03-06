package com.molkky.molkky.entity.rounds;

import com.molkky.molkky.MolkkyApplication;
import com.molkky.molkky.domain.Phase;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.rounds.Finnish;
import com.molkky.molkky.domain.rounds.Knockout;
import com.molkky.molkky.model.phase.PhaseModel;
import com.molkky.molkky.repository.PhaseRepository;
import com.molkky.molkky.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import type.TournamentStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = MolkkyApplication.class)
class KnockoutEntityTest {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Test
    @Rollback(false)
    @Transactional
    void testCreateKnockout() {
        Tournament tournament = new Tournament(
                "tournament test",
                "location",
                new Date(),
                new Date(),
                1,
                8,
                true,
                true,
                2,
                3,
                2
        );
        tournament.setNbPlayersPerTeam(1);
        tournament.setVisible(true);
        tournament.setStatus(TournamentStatus.AVAILABLE);
        tournament= tournamentRepository.save(tournament);

        Knockout knockout = new Knockout();

        knockout.setNbPhase(1);
        knockout.setTournament(tournament);
        knockout.setNbSets(3);

        knockout = phaseRepository.save(knockout);

        List<Phase> phases = new ArrayList<>();
        phases.add(knockout);
        tournament.setPhases(phases);
        tournamentRepository.save(tournament);


        Assertions.assertEquals(1, tournament.getPhases().size(), "Tournament should have 1 phase1");
        Assertions.assertEquals(true, tournament.getPhases().get(0) instanceof Knockout,
                " It should be a instance of knockout");

    }

    @Test
    void testEditKnockoutInfo(){
        PhaseModel phaseModel = new PhaseModel();
        phaseModel.setRandomDraw(true);
        phaseModel.setNotifEveryRound(true);
        phaseModel.setTimePhase("");
        phaseModel.setHourPhaseStart("");
        Knockout knockout = new Knockout();
        knockout.editInfoKnockout(phaseModel);

        Assertions.assertTrue(knockout.isRandomDraw());
        Assertions.assertTrue(knockout.isNotifEveryRound());
    }
}
