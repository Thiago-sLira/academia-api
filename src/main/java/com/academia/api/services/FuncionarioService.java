package com.academia.api.services;

import com.academia.api.dto.requests.FuncionarioRequestDTO;
import com.academia.api.dto.responses.FuncionarioResponseDTO;
import com.academia.api.dto.requests.LoginRequestDTO;
import com.academia.api.exception.CredenciaisInvalidasException;
import com.academia.api.models.entities.Funcionario;
import com.academia.api.repositories.FuncionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public FuncionarioResponseDTO cadastrar(FuncionarioRequestDTO dto) {
        Funcionario funcionario = Funcionario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .registroAcademico(dto.registroAcademico())
                .perfil(dto.perfil())
                .ativo(true)
                .build();

        return new FuncionarioResponseDTO(repository.save(funcionario));
    }

    public List<FuncionarioResponseDTO> listarTodos() {
        return repository.findAll().stream().map(FuncionarioResponseDTO::new).toList();
    }

    public List<FuncionarioResponseDTO> listarAtivos() {
        return repository.findByAtivoTrue().stream().map(FuncionarioResponseDTO::new).toList();
    }

    public FuncionarioResponseDTO buscarPorId(Long id) {
        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + id));
        return new FuncionarioResponseDTO(funcionario);
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + id));

        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());
        funcionario.setRegistroAcademico(dto.registroAcademico());
        funcionario.setPerfil(dto.perfil());

        return new FuncionarioResponseDTO(repository.save(funcionario));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Funcionário não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    public FuncionarioResponseDTO login(LoginRequestDTO dto) {
        Funcionario funcionario = repository.findByEmail(dto.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(dto.senha(), funcionario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        return new FuncionarioResponseDTO(funcionario);
    }
}
