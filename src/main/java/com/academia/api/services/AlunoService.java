package com.academia.api.services;

import com.academia.api.dtos.requests.AlunoFiltroDTO;
import com.academia.api.dtos.requests.AlunoRequestDTO;
import com.academia.api.dtos.responses.AlunoPaginadoResponseDTO;
import com.academia.api.dtos.responses.AlunoResponseDTO;
import com.academia.api.exceptions.AlunoNaoEncontradoException;
import com.academia.api.models.entities.Aluno;
import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;
import com.academia.api.repositories.AlunoRepository;
import com.academia.api.specifications.AlunoSpecification;

import com.academia.api.validation.EnumNormalizer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .genero(EnumNormalizer.parseEnum(Genero.class, dto.genero()).orElse(null))
                .nivelExperiencia(EnumNormalizer.parseEnum(NivelExperiencia.class, dto.nivelExperiencia()).orElse(null))
                .diasDisponiveisSemana(dto.diasDisponiveisSemana())
                .restricaoMedica(dto.restricaoMedica())
                .ativo(true)
                .build();

        return new AlunoResponseDTO(repository.save(aluno));
    }

    public AlunoPaginadoResponseDTO listarTodos(AlunoFiltroDTO filtro) {
        Page<Aluno> page = repository.findAll(
                AlunoSpecification.filtrar(
                        filtro.id(),
                        filtro.nome(),
                        filtro.email(),
                        filtro.telefone(),
                        filtro.idade(),
                        filtro.peso(),
                        filtro.altura(),
                        filtro.genero(),
                        filtro.nivelExperiencia()
                ),
                filtro.toPageable()
        );

        return new AlunoPaginadoResponseDTO(
                page.map(AlunoResponseDTO::new).getContent(),
                page.getTotalElements()
        );
    }

    @Transactional
    public AlunoResponseDTO atualizar(Long id, AlunoRequestDTO dto) {
        Aluno aluno = repository.findById(id).orElseThrow(() -> new AlunoNaoEncontradoException(id));

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
