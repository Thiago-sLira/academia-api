package com.academia.api.repositories;

import com.academia.api.models.entities.PlanoTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanoTreinoRepository extends JpaRepository<PlanoTreino, Long>,
        JpaSpecificationExecutor<PlanoTreino> {
}
