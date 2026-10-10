
package com.academia.api.controllers;

import com.academia.api.dtos.requests.AlunoFiltroDTO;
import com.academia.api.dtos.requests.AlunoRequestDTO;
import com.academia.api.dtos.responses.AlunoPaginadoResponseDTO;
import com.academia.api.dtos.responses.AlunoResponseDTO;
import com.academia.api.services.AlunoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
@Tag(name = "Alunos", description = "Endpoints para gerenciamento de alunos da academia")
public class AlunoController {

    private final AlunoService service;

    @GetMapping
    @Operation(summary = "Listar alunos com filtros e paginação")
    public ResponseEntity<AlunoPaginadoResponseDTO> listar(@ParameterObject @ModelAttribute AlunoFiltroDTO filtro) {
        return ResponseEntity.ok(service.listarTodos(filtro));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo aluno (Fase 1)")
    public ResponseEntity<AlunoResponseDTO> cadastrar(@RequestBody @Valid AlunoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de um aluno")
    public ResponseEntity<AlunoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AlunoRequestDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um aluno")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
