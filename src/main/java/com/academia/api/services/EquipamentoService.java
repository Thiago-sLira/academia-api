package com.academia.api.services;

import com.academia.api.dtos.requests.EquipamentoFiltroDTO;
import com.academia.api.dtos.responses.EquipamentoPaginadoResponseDTO;
import com.academia.api.dtos.responses.EquipamentoResponseDTO;
import com.academia.api.models.entities.Equipamento;
import com.academia.api.repositories.EquipamentoRepository;
import com.academia.api.specifications.EquipamentoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
}
