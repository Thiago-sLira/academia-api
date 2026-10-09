package com.academia.api.services;

import com.academia.api.dtos.requests.AlunoRequestDTO;
import com.academia.api.dtos.responses.AlunoResponseDTO;
import com.academia.api.exceptions.AlunoNaoEncontradoException;
import com.academia.api.models.entities.Aluno;
import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;
import com.academia.api.repositories.AlunoRepository;
import com.academia.api.specifications.AlunoSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
                .genero(com.academia.api.validation.EnumNormalizer.parseEnum(com.academia.api.models.enums.Genero.class, dto.genero()).orElse(null))
                .nivelExperiencia(com.academia.api.validation.EnumNormalizer.parseEnum(com.academia.api.models.enums.NivelExperiencia.class, dto.nivelExperiencia()).orElse(null))
                .diasDisponiveisSemana(dto.diasDisponiveisSemana())
                .restricaoMedica(dto.restricaoMedica())
                .ativo(true)
                .build();

        return new AlunoResponseDTO(repository.save(aluno));
    }




    public Page<AlunoResponseDTO> listarTodos(
            Long id,
            String nome,
            String email,
            String telefone,
            Integer idade,
            BigDecimal peso,
            BigDecimal altura,
            Genero genero,
            NivelExperiencia nivelExperiencia,
            int pagina,
            int tamanhoPagina
        ) {
        if (pagina < 0) {
            throw new IllegalArgumentException("A página não pode ser negativa.");
        }

        if (tamanhoPagina < 1) {
            throw new IllegalArgumentException("O tamanho da página deve ser maior que zero.");
        }

        Pageable pageable = PageRequest.of(
            pagina,
            tamanhoPagina,
            org.springframework.data.domain.Sort.by("id").ascending()
        );

        return repository.findAll(
                AlunoSpecification.filtrar(
                        id,
                        nome,
                        email,
                        telefone,
                        idade,
                        peso,
                        altura,
                        genero,
                        nivelExperiencia
                ),
                pageable
        ).map(AlunoResponseDTO::new);
    }


    public AlunoResponseDTO buscarPorId(Long id) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new AlunoNaoEncontradoException(id));
        return new AlunoResponseDTO(aluno);
    }

    @Transactional
    public AlunoResponseDTO atualizar(Long id, AlunoRequestDTO dto) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new AlunoNaoEncontradoException(id));

        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setTelefone(dto.telefone());
        aluno.setIdade(dto.idade());
        aluno.setPeso(dto.peso());
        aluno.setAltura(dto.altura());
        aluno.setGenero(com.academia.api.validation.EnumNormalizer.parseEnum(com.academia.api.models.enums.Genero.class, dto.genero()).orElse(null));
        aluno.setNivelExperiencia(com.academia.api.validation.EnumNormalizer.parseEnum(com.academia.api.models.enums.NivelExperiencia.class, dto.nivelExperiencia()).orElse(null));
        aluno.setDiasDisponiveisSemana(dto.diasDisponiveisSemana());
        aluno.setRestricaoMedica(dto.restricaoMedica());

        return new AlunoResponseDTO(repository.save(aluno));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new AlunoNaoEncontradoException(id);
        }
        repository.deleteById(id);
    }
}