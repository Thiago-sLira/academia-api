package com.academia.api.controllers;

import com.academia.api.dtos.responses.TipoTreinoResponseDTO;
import com.academia.api.services.TipoTreinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-treino")
@RequiredArgsConstructor
@Tag(name = "Tipos de Treino", description = "Endpoints para consulta dos tipos de treino disponíveis")
public class TipoTreinoController {

    private final TipoTreinoService service;

    @GetMapping
    @Operation(summary = "Listar todos os tipos de treino")
    public ResponseEntity<List<TipoTreinoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
