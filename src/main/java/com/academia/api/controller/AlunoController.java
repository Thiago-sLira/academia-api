package com.academia.api.controller;

import com.academia.api.dto.AlunoRequestDTO;
import com.academia.api.dto.AlunoResponseDTO;
import com.academia.api.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
@Tag(name = "Alunos", description = "Endpoints para gerenciamento de alunos da academia")
public class AlunoController {

    private final AlunoService service;

    @GetMapping
    @Operation(summary = "Listar todos os alunos")
    public ResponseEntity<List<AlunoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID")
    public ResponseEntity<AlunoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo aluno (Fase 1)")
    public ResponseEntity<AlunoResponseDTO> cadastrar(@RequestBody @Valid AlunoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de um aluno")
    public ResponseEntity<AlunoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AlunoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um aluno")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}