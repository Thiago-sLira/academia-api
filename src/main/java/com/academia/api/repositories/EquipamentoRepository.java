package com.academia.api.repositories;

import com.academia.api.models.entities.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Integer>, JpaSpecificationExecutor<Equipamento> {
}
