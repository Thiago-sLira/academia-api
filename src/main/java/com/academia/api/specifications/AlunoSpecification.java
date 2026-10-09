
package com.academia.api.specifications;

import com.academia.api.models.entities.Aluno;
import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Locale;

public class AlunoSpecification {

    private AlunoSpecification() {
    }

    public static Specification<Aluno> filtrar(
            Long id,
            String nome,
            String email,
            String telefone,
            Integer idade,
            BigDecimal peso,
            BigDecimal altura,
            Genero genero,
            NivelExperiencia nivelExperiencia
    ) {
        return (root, query, cb) -> {
            var filtros = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            if (id != null) {
                filtros.add(cb.equal(root.get("id"), id));
            }

            if (nome != null && !nome.isBlank()) {
                filtros.add(cb.like(
                        cb.lower(root.get("nome")),
                        "%" + nome.toLowerCase(Locale.ROOT).trim() + "%"
                ));
            }

            if (email != null && !email.isBlank()) {
                filtros.add(cb.like(
                        cb.lower(root.get("email")),
                        "%" + email.toLowerCase(Locale.ROOT).trim() + "%"
                ));
            }

            if (telefone != null && !telefone.isBlank()) {
                filtros.add(cb.like(
                        root.get("telefone"),
                        "%" + telefone.trim() + "%"
                ));
            }

            if (idade != null) {
                filtros.add(cb.equal(root.get("idade"), idade));
            }

            if (peso != null) {
                filtros.add(cb.equal(root.get("peso"), peso));
            }

            if (altura != null) {
                filtros.add(cb.equal(root.get("altura"), altura));
            }

            if (genero != null) {
                filtros.add(cb.equal(root.get("genero"), genero));
            }

            if (nivelExperiencia != null) {
                filtros.add(cb.equal(
                        root.get("nivelExperiencia"),
                        nivelExperiencia
                ));
            }

            return cb.and(filtros.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
