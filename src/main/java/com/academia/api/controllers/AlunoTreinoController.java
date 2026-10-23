package com.academia.api.controllers;

import com.academia.api.dtos.requests.VincularTreinoRequestDTO;
import com.academia.api.dtos.responses.AlunoTreinoResponseDTO;
import com.academia.api.services.AlunoTreinoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos/{alunoId}/treinos")
public class AlunoTreinoController {

    @Autowired
    private AlunoTreinoService alunoTreinoService;

    @PostMapping
    public ResponseEntity<AlunoTreinoResponseDTO> vincularTreino(
            @PathVariable Long alunoId,
            @RequestBody @Valid VincularTreinoRequestDTO request) {
        
        // Garante que o ID do path seja o mesmo do body
        VincularTreinoRequestDTO requestAjustado = new VincularTreinoRequestDTO(
                alunoId, 
                request.planoTreinoId(), 
                request.dataInicio()
        );
        
        AlunoTreinoResponseDTO response = alunoTreinoService.vincularTreino(requestAjustado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AlunoTreinoResponseDTO>> listarTreinosDoAluno(@PathVariable Long alunoId) {
        List<AlunoTreinoResponseDTO> treinos = alunoTreinoService.listarTreinosPorAluno(alunoId);
        return ResponseEntity.ok(treinos);
    }
}
