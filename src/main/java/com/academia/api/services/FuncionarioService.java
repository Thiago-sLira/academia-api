package com.academia.api.services;

import com.academia.api.dtos.requests.FuncionarioRequestDTO;
import com.academia.api.dtos.requests.LoginRequestDTO;
import com.academia.api.dtos.responses.FuncionarioResponseDTO;
import com.academia.api.dtos.responses.LoginResponseDTO;
import com.academia.api.exceptions.CredenciaisInvalidasException;
import com.academia.api.exceptions.FuncionarioNaoEncontradoException;
import com.academia.api.models.entities.Funcionario;
import com.academia.api.models.enums.PerfilFuncionario;
import com.academia.api.repositories.FuncionarioRepository;
import com.academia.api.validation.EnumNormalizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public FuncionarioService(FuncionarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public FuncionarioResponseDTO cadastrar(FuncionarioRequestDTO dto) {
        Funcionario funcionario = Funcionario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .registroAcademico(dto.registroAcademico())
                .perfil(EnumNormalizer.parseEnum(PerfilFuncionario.class, dto.perfil()).orElse(null))
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
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
        return new FuncionarioResponseDTO(funcionario);
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));

        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());
        funcionario.setRegistroAcademico(dto.registroAcademico());
        funcionario.setPerfil(EnumNormalizer.parseEnum(PerfilFuncionario.class, dto.perfil()).orElse(null));

        return new FuncionarioResponseDTO(repository.save(funcionario));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new FuncionarioNaoEncontradoException(id);
        }
        repository.deleteById(id);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Funcionario funcionario = repository.findByEmail(dto.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(dto.senha(), funcionario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        String token = jwtService.gerarToken(funcionario);

        return new LoginResponseDTO(
                token,
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getPerfil()
        );
    }
}
