package com.molkky.molkky.repository;

import com.molkky.molkky.domain.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TournamentRepository extends JpaRepository<Tournament, String>, JpaSpecificationExecutor<Tournament> {
    Tournament findById(Integer id);
    Tournament findByName(String tournamentName);
    List<Tournament> findTournamentsByVisibleAndStarted(boolean visible, boolean started);
    List<Tournament> findTournamentsByVisibleAndFinished(boolean visible, boolean finished);
    Tournament findByStatus(String status);
}
