package com.academia.api.models.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_aluno_treino")

public class AlunoTreino {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vinculo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plano", nullable = false)
    private PlanoTreino planoTreino;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_previsao_fim")
    private LocalDate dataPrevisaoFim;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    // Construtores, Getters e Setters
    public AlunoTreino() {}

    public AlunoTreino(Aluno aluno, PlanoTreino planoTreino, LocalDate dataInicio) {
        this.aluno = aluno;
        this.planoTreino = planoTreino;
        this.dataInicio = dataInicio;
        this.status = "ATIVO";
    }

    // Gere os Getters e Setters aqui (ou use Lombok se o projeto já usar)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public PlanoTreino getPlanoTreino() { return planoTreino; }
    public void setPlanoTreino(PlanoTreino planoTreino) { this.planoTreino = planoTreino; }
    
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDate getDataPrevisaoFim() { return dataPrevisaoFim; }
    public void setDataPrevisaoFim(LocalDate dataPrevisaoFim) { this.dataPrevisaoFim = dataPrevisaoFim; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
