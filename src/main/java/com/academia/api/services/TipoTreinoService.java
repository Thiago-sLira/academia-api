package com.academia.api.services;

import com.academia.api.dtos.responses.TipoTreinoResponseDTO;
import com.academia.api.repositories.TipoTreinoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoTreinoService {

    private final TipoTreinoRepository repository;

    public TipoTreinoService(TipoTreinoRepository repository) {
        this.repository = repository;
    }

    public List<TipoTreinoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(TipoTreinoResponseDTO::new).toList();
    }
}
