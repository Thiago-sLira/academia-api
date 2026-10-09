package com.academia.api.controllers;

import com.academia.api.dtos.requests.FuncionarioRequestDTO;
import com.academia.api.dtos.requests.LoginRequestDTO;
import com.academia.api.dtos.responses.FuncionarioResponseDTO;
import com.academia.api.dtos.responses.LoginResponseDTO;
import com.academia.api.services.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.academia.api.models.enums.PerfilFuncionario;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
@Tag(name = "Funcionários", description = "Endpoints para gerenciamento de funcionários e autenticação")
public class FuncionarioController {

    private final FuncionarioService service;


    @GetMapping
    @Operation(summary = "Listar funcionários com filtros e paginação")
    public ResponseEntity<Page<FuncionarioResponseDTO>> listar(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String registroAcademico,
            @RequestParam(required = false) PerfilFuncionario perfil,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanhoPagina
    ) {
        return ResponseEntity.ok(service.listarTodos(
                id,
                nome,
                email,
                registroAcademico,
                perfil,
                ativo,
                pagina,
                tamanhoPagina
        ));
    }


    @GetMapping("/ativos")
    @Operation(summary = "Listar apenas funcionários ativos")
    public ResponseEntity<List<FuncionarioResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }
}
