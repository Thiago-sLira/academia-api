package com.academia.api.controller;

import com.academia.api.dto.FuncionarioRequestDTO;
import com.academia.api.dto.FuncionarioResponseDTO;
import com.academia.api.dto.LoginRequestDTO;
import com.academia.api.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
@Tag(name = "Funcionários", description = "Endpoints para gerenciamento de funcionários e autenticação")
public class FuncionarioController {

    private final FuncionarioService service;

    @GetMapping
    @Operation(summary = "Listar todos os funcionários")
    public ResponseEntity<List<FuncionarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar apenas funcionários ativos")
    public ResponseEntity<List<FuncionarioResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar funcionário por ID")
    public ResponseEntity<FuncionarioResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo funcionário")
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(@RequestBody @Valid FuncionarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de um funcionário")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FuncionarioRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um funcionário")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar funcionário por email e senha")
    public ResponseEntity<FuncionarioResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }
}
