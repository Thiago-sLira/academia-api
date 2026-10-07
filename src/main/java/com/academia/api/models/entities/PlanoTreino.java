package com.academia.api.models.entities;

import com.academia.api.models.enums.NivelExperiencia;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_plano_treino")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_treino", nullable = false)
    private TipoTreino tipoTreino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor_criador", nullable = false)
    private Funcionario professorCriador;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 250)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "nivel_recomendado", columnDefinition = "nivel_experiencia")
    private NivelExperiencia nivelRecomendado;

    @Builder.Default
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;
}
