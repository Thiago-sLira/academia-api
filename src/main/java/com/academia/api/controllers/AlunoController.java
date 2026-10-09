
package com.academia.api.controllers;

import com.academia.api.dtos.requests.AlunoRequestDTO;
import com.academia.api.dtos.responses.AlunoResponseDTO;
import com.academia.api.models.enums.Genero;
import com.academia.api.models.enums.NivelExperiencia;
import com.academia.api.services.AlunoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
@Tag(name = "Alunos", description = "Endpoints para gerenciamento de alunos da academia")
public class AlunoController {

    private final AlunoService service;

    @GetMapping
    @Operation(summary = "Listar alunos com filtros e paginação")
    public ResponseEntity<Page<AlunoResponseDTO>> listar(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) Integer idade,
            @RequestParam(required = false) BigDecimal peso,
            @RequestParam(required = false) BigDecimal altura,
            @RequestParam(required = false) Genero genero,
            @RequestParam(required = false) NivelExperiencia nivelExperiencia,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanhoPagina
    ) {
        return ResponseEntity.ok(service.listarTodos(
                id,
                nome,
                email,
                telefone,
                idade,
                peso,
                altura,
                genero,
                nivelExperiencia,
                pagina,
                tamanhoPagina
        ));
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
