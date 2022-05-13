package com.molkky.molkky.entity.rounds;

import com.molkky.molkky.MolkkyApplication;
import com.molkky.molkky.domain.Phase;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.rounds.SimpleGame;
import com.molkky.molkky.repository.*;
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
class SimpleGameEntityTest {
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
                2,
                3,
                2
        ));
                3
        );
        tournament.setNbPlayersPerTeam(1);
        tournament.setVisible(true);
        tournament.setStatus(TournamentStatus.AVAILABLE);
        tournament= tournamentRepository.save(tournament);

        SimpleGame simpleGame = new SimpleGame();

        simpleGame.setNbPhase(1);
        simpleGame.setTournament(tournament);
        simpleGame.setNbSets(3);

        simpleGame = phaseRepository.save(simpleGame);

        List<Phase> phases = new ArrayList<>();
        phases.add(simpleGame);
        tournament.setPhases(phases);
        tournamentRepository.save(tournament);


        Assertions.assertEquals(1, tournament.getPhases().size(), "Tournament should have 2 phases");
        Assertions.assertEquals(true, tournament.getPhases().get(0) instanceof SimpleGame,
                " It should be a instance of simple game");

    }
}
