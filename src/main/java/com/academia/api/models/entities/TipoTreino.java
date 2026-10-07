package com.academia.api.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_tipo_treino")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_treino")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 500)
    private String descricao;
}
