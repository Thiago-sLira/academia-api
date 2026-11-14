package com.academia.api.services;

import com.academia.api.dtos.requests.EquipamentoFiltroDTO;
import com.academia.api.dtos.requests.EquipamentoRequestDTO;
import com.academia.api.dtos.responses.EquipamentoPaginadoResponseDTO;
import com.academia.api.dtos.responses.EquipamentoResponseDTO;
import com.academia.api.exceptions.EquipamentoDuplicadoException;
import com.academia.api.models.entities.Equipamento;
import com.academia.api.models.enums.CategoriaEquipamento;
import com.academia.api.repositories.EquipamentoRepository;
import com.academia.api.specifications.EquipamentoSpecification;
import com.academia.api.validation.EnumNormalizer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository repository;

    public EquipamentoService(EquipamentoRepository repository) {
        this.repository = repository;
    }

    public EquipamentoPaginadoResponseDTO listarTodos(EquipamentoFiltroDTO filtro) {
        Page<Equipamento> page = repository.findAll(
                EquipamentoSpecification.filtrar(
                        filtro.id(),
                        filtro.nome(),
                        filtro.categoria()
                ),
                filtro.toPageable()
        );

        List<EquipamentoResponseDTO> dtos = page.getContent().stream()
                .map(EquipamentoResponseDTO::new)
                .toList();

        return new EquipamentoPaginadoResponseDTO(dtos, page.getTotalElements());
    }

    @Transactional
    public EquipamentoResponseDTO cadastrar(EquipamentoRequestDTO dto) {
        String nome = dto.nome().strip();

        if (repository.existsByNomeIgnoreCase(nome)) {
            throw new EquipamentoDuplicadoException("Já existe um equipamento cadastrado com este nome");
        }

        Equipamento equipamento = Equipamento.builder()
                .nome(nome)
                .categoria(EnumNormalizer.parseEnum(CategoriaEquipamento.class, dto.categoria()).orElse(null))
                .descricao(dto.descricao())
                .ativo(dto.ativo() != null ? dto.ativo() : true)
                .build();

        return new EquipamentoResponseDTO(repository.save(equipamento));
    }
}
