package com.molkky.molkky.entity.rounds;

import com.molkky.molkky.MolkkyApplication;
import com.molkky.molkky.domain.Match;
import com.molkky.molkky.domain.Phase;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.rounds.Finnish;
import com.molkky.molkky.domain.rounds.SimpleGame;
import com.molkky.molkky.domain.rounds.SwissPool;
import com.molkky.molkky.model.phase.PhaseModel;
import com.molkky.molkky.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import type.TournamentStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = MolkkyApplication.class)
class SwissPoolEntityTest {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Test
    @Rollback(false)
    @Transactional
    void testInsertTournamentWithRound() {
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

        SwissPool swissPool = new SwissPool();

        swissPool.setNbPhase(1);
        swissPool.setTournament(tournament);
        swissPool.setNbSets(3);

        swissPool = phaseRepository.save(swissPool);

        List<Phase> phases = new ArrayList<>();
        phases.add(swissPool);
        tournament.setPhases(phases);
        tournamentRepository.save(tournament);


        Assertions.assertEquals(1, tournament.getPhases().size(), "Tournament should have 2 phases");
        Assertions.assertEquals(true, tournament.getPhases().get(0) instanceof SwissPool,
                " It should be a instance of simple game");

    }

    @Test
    void testEditSwissInfo(){
        PhaseModel phaseModel = new PhaseModel();
        phaseModel.setNbSubRounds(1);
        phaseModel.setTimePhase("");
        phaseModel.setHourPhaseStart("");
        SwissPool swissPool = new SwissPool();
        swissPool.editInfoSwiss(phaseModel);

        Assertions.assertEquals(1,swissPool.getNbSubRounds());
    }
}
