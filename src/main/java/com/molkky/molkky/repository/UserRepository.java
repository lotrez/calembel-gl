package com.molkky.molkky.repository;

import com.molkky.molkky.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User findById(Integer id);
    List<User> findAll();

    User findUserByEmail(String email);
    User findUserByEmailAndPassword(String email, String password);
    List<User> findUsersByPseudo(String pseudo);



    boolean existsUserByPseudo(String pseudo);
    boolean existsUserByEmailAndPassword(String email, String password);
    boolean existsUserByEmail(String email);

    @Query(value = "SELECT * FROM user u WHERE u.forename LIKE %?1% OR u.surname LIKE %?1% LIMIT 0,?2",
            nativeQuery = true)
    List<User> searchUsersByName(String searchTerm, Integer n);
}

