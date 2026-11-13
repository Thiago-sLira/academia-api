package com.academia.api.repositories;

import com.academia.api.models.entities.PlanoTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanoTreinoRepository extends JpaRepository<PlanoTreino, Long>,
        JpaSpecificationExecutor<PlanoTreino> {

    @Query("SELECT DISTINCT p FROM PlanoTreino p LEFT JOIN FETCH p.equipamentos WHERE p.id IN :ids")
    List<PlanoTreino> findAllWithEquipamentosByIdIn(@Param("ids") List<Long> ids);
}
