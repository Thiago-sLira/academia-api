package com.academia.api.models.entities;

import com.academia.api.models.enums.NivelExperiencia;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_plano_equipamento",
            joinColumns = @JoinColumn(name = "id_plano"),
            inverseJoinColumns = @JoinColumn(name = "id_equipamento")
    )
    @Builder.Default
    private List<Equipamento> equipamentos = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;
}
