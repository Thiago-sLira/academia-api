package com.academia.api.services;

import com.academia.api.dtos.requests.VincularTreinoRequestDTO;
import com.academia.api.dtos.responses.AlunoTreinoResponseDTO;
import com.academia.api.exceptions.AlunoNaoEncontradoException;
import com.academia.api.exceptions.PlanoTreinoNaoEncontradoException;
import com.academia.api.models.entities.Aluno;
import com.academia.api.models.entities.AlunoTreino;
import com.academia.api.models.entities.PlanoTreino;
import com.academia.api.repositories.AlunoRepository;
import com.academia.api.repositories.AlunoTreinoRepository;
import com.academia.api.repositories.PlanoTreinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoTreinoService {

    @Autowired
    private AlunoTreinoRepository alunoTreinoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PlanoTreinoRepository planoTreinoRepository;

    @Transactional
    public AlunoTreinoResponseDTO vincularTreino(VincularTreinoRequestDTO request) {
        // CORREÇÃO AQUI: Passando apenas o ID para a exceção
        Aluno aluno = alunoRepository.findById(request.alunoId())
                .orElseThrow(() -> new AlunoNaoEncontradoException(request.alunoId()));

        // CORREÇÃO AQUI: Passando apenas o ID para a exceção
        PlanoTreino planoTreino = planoTreinoRepository.findById(request.planoTreinoId())
                .orElseThrow(() -> new PlanoTreinoNaoEncontradoException(request.planoTreinoId()));

        if (alunoTreinoRepository.existsByAlunoIdAndPlanoTreinoIdAndStatus(aluno.getId(), planoTreino.getId(), "ATIVO")) {
            throw new RuntimeException("Este aluno já possui este treino vinculado e ativo.");
        }

        AlunoTreino alunoTreino = new AlunoTreino(aluno, planoTreino, request.dataInicio());
        alunoTreino = alunoTreinoRepository.save(alunoTreino);

        return toResponseDTO(alunoTreino);
    }

    public List<AlunoTreinoResponseDTO> listarTreinosPorAluno(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            // CORREÇÃO AQUI: Passando apenas o ID para a exceção
            throw new AlunoNaoEncontradoException(alunoId);
        }
        
        return alunoTreinoRepository.findByAlunoId(alunoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private AlunoTreinoResponseDTO toResponseDTO(AlunoTreino entity) {
        return new AlunoTreinoResponseDTO(
            entity.getId(),
            entity.getAluno().getId(),
            entity.getAluno().getNome(), // Certifique-se de que Aluno tem getNome()
            entity.getPlanoTreino().getId(),
            entity.getPlanoTreino().getTitulo(), // CORRIGIDO: Alterado de getNome() para getTitulo()
            entity.getDataInicio(),
            entity.getDataPrevisaoFim(),
            "ATIVO".equals(entity.getStatus())
        );
    }
}
