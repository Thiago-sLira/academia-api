package com.academia.api.specifications;

import com.academia.api.models.entities.Equipamento;
import com.academia.api.models.enums.CategoriaEquipamento;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Locale;

public class EquipamentoSpecification {

    private EquipamentoSpecification() {
    }

    public static Specification<Equipamento> filtrar(
            Integer id,
            String nome,
            CategoriaEquipamento categoria
    ) {
        return (root, query, cb) -> {
            var filtros = new ArrayList<Predicate>();

            if (id != null) {
                filtros.add(cb.equal(root.get("id"), id));
            }

            if (nome != null && !nome.isBlank()) {
                filtros.add(cb.like(
                        cb.lower(root.get("nome")),
                        "%" + nome.toLowerCase(Locale.ROOT).trim() + "%"
                ));
            }

            if (categoria != null) {
                filtros.add(cb.equal(root.get("categoria"), categoria));
            }

            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }
}
