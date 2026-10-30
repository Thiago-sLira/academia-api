package com.academia.api.services;

import com.academia.api.dtos.requests.PlanoTreinoFiltroDTO;
import com.academia.api.dtos.requests.PlanoTreinoRequestDTO;
import com.academia.api.dtos.responses.AlunoResumoDTO;
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
import com.academia.api.models.enums.StatusVinculo;
import com.academia.api.repositories.AlunoTreinoRepository;
import com.academia.api.repositories.FuncionarioRepository;
import com.academia.api.repositories.PlanoTreinoRepository;
import com.academia.api.repositories.TipoTreinoRepository;
import com.academia.api.validation.EnumNormalizer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlanoTreinoService {

    private final PlanoTreinoRepository planoTreinoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TipoTreinoRepository tipoTreinoRepository;
    private final AlunoTreinoRepository alunoTreinoRepository;

    public PlanoTreinoService(PlanoTreinoRepository planoTreinoRepository,
                              FuncionarioRepository funcionarioRepository,
                              TipoTreinoRepository tipoTreinoRepository,
                              AlunoTreinoRepository alunoTreinoRepository) {
        this.planoTreinoRepository = planoTreinoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.tipoTreinoRepository = tipoTreinoRepository;
        this.alunoTreinoRepository = alunoTreinoRepository;
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

    public PlanoTreinoPaginadoResponseDTO listar(PlanoTreinoFiltroDTO filtro) {
        Specification<PlanoTreino> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.idTipoTreino() != null) {
                predicates.add(cb.equal(root.get("tipoTreino").get("id"), filtro.idTipoTreino()));
            }
            if (filtro.idProfessorCriador() != null) {
                predicates.add(cb.equal(root.get("professorCriador").get("id"), filtro.idProfessorCriador()));
            }
            if (filtro.nivelRecomendado() != null && !filtro.nivelRecomendado().isBlank()) {
                NivelExperiencia nivel = EnumNormalizer.parseEnum(NivelExperiencia.class, filtro.nivelRecomendado())
                        .orElse(null);
                if (nivel != null) {
                    predicates.add(cb.equal(root.get("nivelRecomendado"), nivel));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<PlanoTreino> pagina = planoTreinoRepository.findAll(spec, filtro.toPageable());

        List<Long> planIds = pagina.map(PlanoTreino::getId).getContent();

        Map<Long, List<AlunoResumoDTO>> alunosMap = planIds.isEmpty()
                ? Collections.emptyMap()
                : alunoTreinoRepository.findByPlanoTreinoIdInAndStatus(planIds, StatusVinculo.ATIVO)
                        .stream()
                        .collect(Collectors.groupingBy(
                                at -> at.getPlanoTreino().getId(),
                                Collectors.mapping(
                                        at -> new AlunoResumoDTO(
                                                at.getAluno().getId(),
                                                at.getAluno().getNome(),
                                                at.getAluno().getEmail()),
                                        Collectors.toList())));

        List<PlanoTreinoListagemResponseDTO> dtos = pagina.getContent().stream()
                .map(plano -> new PlanoTreinoListagemResponseDTO(
                        plano,
                        alunosMap.getOrDefault(plano.getId(), List.of())))
                .collect(Collectors.toList());

        return new PlanoTreinoPaginadoResponseDTO(dtos, pagina.getTotalElements());
    }
}
