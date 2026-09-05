package com.academia.api.models.entities;

import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    private Integer idade;

    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(precision = 3, scale = 2)
    private BigDecimal altura;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "genero_enum")
    private Genero genero;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "nivel_experiencia", columnDefinition = "nivel_experiencia")
    private NivelExperiencia nivelExperiencia;

    @Column(name = "dias_disponiveis_semana")
    private Integer diasDisponiveisSemana;

    @Column(name = "restricao_medica", columnDefinition = "TEXT")
    private String restricaoMedica;

    private Boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime timestampCriacao;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime timestampUltimaAtualizacao;
}