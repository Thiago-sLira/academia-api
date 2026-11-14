package com.academia.api.controllers;

import com.academia.api.dtos.requests.EquipamentoFiltroDTO;
import com.academia.api.dtos.requests.EquipamentoRequestDTO;
import com.academia.api.dtos.responses.EquipamentoPaginadoResponseDTO;
import com.academia.api.dtos.responses.EquipamentoResponseDTO;
import com.academia.api.services.EquipamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipamentos")
@RequiredArgsConstructor
@Tag(name = "Equipamentos", description = "Endpoints para gerenciamento de equipamentos da academia")
public class EquipamentoController {

    private final EquipamentoService service;

    @GetMapping
    @Operation(summary = "Listar equipamentos com filtros e paginação")
    public ResponseEntity<EquipamentoPaginadoResponseDTO> listar(@ParameterObject @ModelAttribute EquipamentoFiltroDTO filtro) {
        return ResponseEntity.ok(service.listarTodos(filtro));
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo equipamento")
    public ResponseEntity<EquipamentoResponseDTO> cadastrar(@RequestBody @Valid EquipamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }
}
