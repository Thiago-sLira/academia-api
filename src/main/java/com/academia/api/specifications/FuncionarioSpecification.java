
package com.academia.api.specifications;

import com.academia.api.models.entities.Funcionario;
import com.academia.api.models.enums.PerfilFuncionario;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FuncionarioSpecification {

    private FuncionarioSpecification() {
    }

    public static Specification<Funcionario> filtrar(
            Long id,
            String nome,
            String email,
            String registroAcademico,
            PerfilFuncionario perfil,
            Boolean ativo
    ) {
        return (root, query, cb) -> {

            List<Predicate> filtros = new ArrayList<>();

            if (id != null) {
                filtros.add(cb.equal(root.get("id"), id));
            }

            if (nome != null && !nome.isBlank()) {
                filtros.add(cb.like(
                        cb.lower(root.get("nome")),
                        "%" + nome.trim().toLowerCase(Locale.ROOT) + "%"
                ));
            }

            if (email != null && !email.isBlank()) {
                filtros.add(cb.like(
                        cb.lower(root.get("email")),
                        "%" + email.trim().toLowerCase(Locale.ROOT) + "%"
                ));
            }

            if (registroAcademico != null && !registroAcademico.isBlank()) {
                filtros.add(cb.like(
                        cb.lower(root.get("registroAcademico")),
                        "%" + registroAcademico.trim().toLowerCase(Locale.ROOT) + "%"
                ));
            }

            if (perfil != null) {
                filtros.add(cb.equal(root.get("perfil"), perfil));
            }

            if (ativo != null) {
                filtros.add(cb.equal(root.get("ativo"), ativo));
            }

            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }
}
