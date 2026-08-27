package com.academia.api.service;

import com.academia.api.dto.AlunoRequestDTO;
import com.academia.api.dto.AlunoResponseDTO;
import com.academia.api.model.Aluno;
import com.academia.api.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AlunoResponseDTO cadastrar(AlunoRequestDTO dto) {
        Aluno aluno = Aluno.builder()
                .nome(dto.nome())
                .email(dto.email())
                .telefone(dto.telefone())
                .idade(dto.idade())
                .peso(dto.peso())
                .altura(dto.altura())
                .genero(dto.genero())
                .nivelExperiencia(dto.nivelExperiencia())
                .diasDisponiveisSemana(dto.diasDisponiveisSemana())
                .restricaoMedica(dto.restricaoMedica())
                .ativo(true)
                .build();

        return new AlunoResponseDTO(repository.save(aluno));
    }

    public List<AlunoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(AlunoResponseDTO::new).toList();
    }

    public AlunoResponseDTO buscarPorId(Long id) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));
        return new AlunoResponseDTO(aluno);
    }

    @Transactional
    public AlunoResponseDTO atualizar(Long id, AlunoRequestDTO dto) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));

        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setTelefone(dto.telefone());
        aluno.setIdade(dto.idade());
        aluno.setPeso(dto.peso());
        aluno.setAltura(dto.altura());
        aluno.setGenero(dto.genero());
        aluno.setNivelExperiencia(dto.nivelExperiencia());
        aluno.setDiasDisponiveisSemana(dto.diasDisponiveisSemana());
        aluno.setRestricaoMedica(dto.restricaoMedica());

        return new AlunoResponseDTO(repository.save(aluno));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}