package com.molkky.molkky.repository;

import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.UserTournamentRole;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTounamentRoleRepository extends JpaRepository<UserTournamentRole, String>, JpaSpecificationExecutor<UserTournamentRole> {

    List<UserTournamentRole> findByUser(User user);


}
