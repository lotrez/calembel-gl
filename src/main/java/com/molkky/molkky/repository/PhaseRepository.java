package com.molkky.molkky.repository;

import com.molkky.molkky.domain.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, String>, JpaSpecificationExecutor<Phase> {
}
