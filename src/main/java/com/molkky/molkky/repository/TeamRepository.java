package com.molkky.molkky.repository;

import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, String>, JpaSpecificationExecutor<Team> {
    Team findById(Integer id);
    Team findByName(String teamName);

    @Query(value = "SELECT * FROM team u WHERE u.name LIKE %?1% LIMIT 0,?2",
            nativeQuery = true)
    List<Team> searchTeamsByName(String searchTerm, Integer n);
    List<Team> findByTournamentAndEliminated(Tournament tournament, Boolean elimitaded);

    List<Team> findTeamByName(String teamName);
    Boolean existsTeamByName(String teamName);
    Boolean existsByCode(String code);
    @Query(value="SELECT t.name FROM Team t")
    List<String> findTeamsName();

}
