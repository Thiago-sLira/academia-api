package com.academia.api.services;

import com.academia.api.dtos.requests.PlanoTreinoRequestDTO;
import com.academia.api.dtos.responses.PlanoTreinoListagemResponseDTO;
import com.academia.api.dtos.responses.PlanoTreinoPaginadoResponseDTO;
import com.academia.api.dtos.responses.PlanoTreinoResponseDTO;
import com.academia.api.exceptions.PerfilNaoAutorizadoException;
import com.academia.api.exceptions.ProfessorNaoEncontradoException;
import com.academia.api.exceptions.TipoTreinoNaoEncontradoException;
import com.academia.api.models.entities.Funcionario;
import com.academia.api.models.entities.PlanoTreino;
import com.academia.api.models.entities.TipoTreino;
import com.academia.api.models.enums.NivelExperiencia;
import com.academia.api.models.enums.PerfilFuncionario;
import com.academia.api.repositories.FuncionarioRepository;
import com.academia.api.repositories.PlanoTreinoRepository;
import com.academia.api.repositories.TipoTreinoRepository;
import com.academia.api.validation.EnumNormalizer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanoTreinoService {

    private final PlanoTreinoRepository planoTreinoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TipoTreinoRepository tipoTreinoRepository;

    public PlanoTreinoService(PlanoTreinoRepository planoTreinoRepository,
                              FuncionarioRepository funcionarioRepository,
                              TipoTreinoRepository tipoTreinoRepository) {
        this.planoTreinoRepository = planoTreinoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.tipoTreinoRepository = tipoTreinoRepository;
    }

    @Transactional
    public PlanoTreinoResponseDTO cadastrar(PlanoTreinoRequestDTO dto) {
        Funcionario professor = funcionarioRepository.findById(dto.idProfessorCriador())
                .orElseThrow(() -> new ProfessorNaoEncontradoException(dto.idProfessorCriador()));

        if (professor.getPerfil() != PerfilFuncionario.PROFESSOR) {
            throw new PerfilNaoAutorizadoException();
        }

        TipoTreino tipoTreino = tipoTreinoRepository.findById(dto.idTipoTreino())
                .orElseThrow(() -> new TipoTreinoNaoEncontradoException(dto.idTipoTreino()));

        NivelExperiencia nivel = EnumNormalizer.parseEnum(NivelExperiencia.class, dto.nivelRecomendado())
                .orElse(null);

        PlanoTreino plano = PlanoTreino.builder()
                .tipoTreino(tipoTreino)
                .professorCriador(professor)
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .nivelRecomendado(nivel)
                .build();

        return new PlanoTreinoResponseDTO(planoTreinoRepository.save(plano));
    }

    public PlanoTreinoPaginadoResponseDTO listar(Long idTipoTreino,
                                                       Long idProfessorCriador,
                                                       String nivelRecomendado,
                                                       Pageable pageable) {
        Specification<PlanoTreino> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (idTipoTreino != null) {
                predicates.add(cb.equal(root.get("tipoTreino").get("id"), idTipoTreino));
            }
            if (idProfessorCriador != null) {
                predicates.add(cb.equal(root.get("professorCriador").get("id"), idProfessorCriador));
            }
            if (nivelRecomendado != null && !nivelRecomendado.isBlank()) {
                NivelExperiencia nivel = EnumNormalizer.parseEnum(NivelExperiencia.class, nivelRecomendado)
                        .orElse(null);
                if (nivel != null) {
                    predicates.add(cb.equal(root.get("nivelRecomendado"), nivel));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        var pagina = planoTreinoRepository.findAll(spec, pageable);
        return new PlanoTreinoPaginadoResponseDTO(
                pagina.map(PlanoTreinoListagemResponseDTO::new).getContent(),
                pagina.getTotalElements()
        );
    }
}
