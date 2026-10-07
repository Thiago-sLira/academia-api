package com.academia.api.repositories;

import com.academia.api.models.entities.TipoTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTreinoRepository extends JpaRepository<TipoTreino, Long> {
}
