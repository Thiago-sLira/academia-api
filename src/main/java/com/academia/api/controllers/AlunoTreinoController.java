package com.academia.api.controllers;

import com.academia.api.dtos.requests.VinculoAlunoTreinoRequestDTO;
import com.academia.api.dtos.responses.VinculoAlunoTreinoResponseDTO;
import com.academia.api.services.AlunoTreinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alunos-treino")
@RequiredArgsConstructor
@Tag(name = "Alunos Treino", description = "Endpoints para gerenciamento de vínculos de alunos e planos de treino")
public class AlunoTreinoController {

    private final AlunoTreinoService alunoTreinoService;

    @PostMapping
    @Operation(summary = "Vincular aluno a plano de treino")
    @ApiResponse(responseCode = "201", description = "Vínculo criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    @ApiResponse(responseCode = "404", description = "Aluno ou plano de treino não encontrado")
    @ApiResponse(responseCode = "409", description = "Conflito — aluno com plano ativo")
    public ResponseEntity<VinculoAlunoTreinoResponseDTO> vincular(@Valid @RequestBody VinculoAlunoTreinoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoTreinoService.vincular(dto));
    }
}
