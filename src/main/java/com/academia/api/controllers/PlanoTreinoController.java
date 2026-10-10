package com.academia.api.controllers;

import com.academia.api.dtos.requests.PlanoTreinoFiltroDTO;
import com.academia.api.dtos.requests.PlanoTreinoRequestDTO;
import com.academia.api.dtos.responses.PlanoTreinoPaginadoResponseDTO;
import com.academia.api.dtos.responses.PlanoTreinoResponseDTO;
import com.academia.api.services.PlanoTreinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planos-treino")
@RequiredArgsConstructor
@Tag(name = "Planos de Treino", description = "Endpoints para gerenciamento de planos de treino")
public class PlanoTreinoController {

    private final PlanoTreinoService service;

    @GetMapping
    @Operation(summary = "Listar planos de treino com paginação e filtros opcionais")
    public ResponseEntity<PlanoTreinoPaginadoResponseDTO> listar(@ParameterObject @ModelAttribute PlanoTreinoFiltroDTO filtro) {
        return ResponseEntity.ok(service.listar(filtro));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo plano de treino")
    public ResponseEntity<PlanoTreinoResponseDTO> cadastrar(@RequestBody @Valid PlanoTreinoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }
}
