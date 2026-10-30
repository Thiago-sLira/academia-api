package com.academia.api.repositories;

import com.academia.api.models.entities.AlunoTreino;
import com.academia.api.models.enums.StatusVinculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface AlunoTreinoRepository extends JpaRepository<AlunoTreino, Long> {

    boolean existsByAlunoIdAndStatus(Long alunoId, StatusVinculo status);

    @Query("SELECT at FROM AlunoTreino at JOIN FETCH at.aluno JOIN FETCH at.planoTreino " +
           "WHERE at.aluno.id IN :ids AND at.status = :status")
    List<AlunoTreino> findByAlunoIdInAndStatus(
            @Param("ids") Collection<Long> ids,
            @Param("status") StatusVinculo status);

    @Query("SELECT at FROM AlunoTreino at JOIN FETCH at.aluno JOIN FETCH at.planoTreino " +
           "WHERE at.planoTreino.id IN :ids AND at.status = :status")
    List<AlunoTreino> findByPlanoTreinoIdInAndStatus(
            @Param("ids") Collection<Long> ids,
            @Param("status") StatusVinculo status);
}
