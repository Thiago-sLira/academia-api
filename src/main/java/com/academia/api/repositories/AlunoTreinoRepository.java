package com.academia.api.repositories;

import com.academia.api.models.entities.AlunoTreino;
import com.academia.api.models.enums.StatusVinculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoTreinoRepository extends JpaRepository<AlunoTreino, Long> {

    boolean existsByAlunoIdAndStatus(Long alunoId, StatusVinculo status);
}
