package com.academia.api.repositories;

import com.academia.api.models.entities.AlunoTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AlunoTreinoRepository extends JpaRepository<AlunoTreino, Long> {
    // Buscar todos os treinos de um aluno específico
    List<AlunoTreino> findByAlunoId(Long alunoId);
    
    // Verificar se já existe um vínculo ativo
    boolean existsByAlunoIdAndPlanoTreinoIdAndStatus(Long alunoId, Long planoTreinoId, String status);
}
