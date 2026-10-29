package com.academia.api.services;

import com.academia.api.dtos.requests.VinculoAlunoTreinoRequestDTO;
import com.academia.api.dtos.responses.VinculoAlunoTreinoResponseDTO;
import com.academia.api.exceptions.AlunoNaoEncontradoException;
import com.academia.api.exceptions.PlanoTreinoNaoEncontradoException;
import com.academia.api.models.entities.Aluno;
import com.academia.api.models.entities.AlunoTreino;
import com.academia.api.models.entities.PlanoTreino;
import com.academia.api.models.enums.StatusVinculo;
import com.academia.api.repositories.AlunoRepository;
import com.academia.api.repositories.AlunoTreinoRepository;
import com.academia.api.repositories.PlanoTreinoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoTreinoService {

    private final AlunoTreinoRepository alunoTreinoRepository;
    private final AlunoRepository alunoRepository;
    private final PlanoTreinoRepository planoTreinoRepository;

    public AlunoTreinoService(AlunoTreinoRepository alunoTreinoRepository,
                               AlunoRepository alunoRepository,
                               PlanoTreinoRepository planoTreinoRepository) {
        this.alunoTreinoRepository = alunoTreinoRepository;
        this.alunoRepository = alunoRepository;
        this.planoTreinoRepository = planoTreinoRepository;
    }

    @Transactional
    public VinculoAlunoTreinoResponseDTO vincular(VinculoAlunoTreinoRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.idAluno())
                .orElseThrow(() -> new AlunoNaoEncontradoException(dto.idAluno()));

        PlanoTreino plano = planoTreinoRepository.findById(dto.idPlano())
                .orElseThrow(() -> new PlanoTreinoNaoEncontradoException(dto.idPlano()));

        AlunoTreino entity = AlunoTreino.builder()
                .aluno(aluno)
                .planoTreino(plano)
                .dataInicio(dto.dataInicio())
                .dataPrevisaoFim(dto.dataPrevisaoFim())
                .status(StatusVinculo.ATIVO)
                .observacoes(dto.observacoes())
                .build();

        return new VinculoAlunoTreinoResponseDTO(alunoTreinoRepository.save(entity));
    }
}
