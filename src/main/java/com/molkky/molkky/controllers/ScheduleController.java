package com.molkky.molkky.controllers;

import com.molkky.molkky.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class ScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private TournamentService tournamentService;

    private int count = 0;

    //@Scheduled(cron = "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week] [Year]")
    // Tous les jours à 1h
    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleFixedDelayTask()  {
        // Reset le compteur qui permet surtout de tester la fonction
        count = 0;
        tournamentService.closeTournamentWhenMinimumTeamsBeforeDate();
        tournamentService.registerClosedForTournament();
        tournamentService.defineMatchInProgress();
        logger.info("Fixed delay task - ${}" , LocalDateTime.now());

        count++;
    }

    public int getCount(){
        return count;
    }
}
