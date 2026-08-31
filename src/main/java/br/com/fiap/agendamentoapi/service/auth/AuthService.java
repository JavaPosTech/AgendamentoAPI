package br.com.fiap.agendamentoapi.service.auth;

import br.com.fiap.agendamentoapi.enums.SituacaoCadastro;
import br.com.fiap.agendamentoapi.exceptions.SenhaIncorretaException;
import br.com.fiap.agendamentoapi.exceptions.UsuarioInativoException;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import br.com.fiap.agendamentoapi.model.request.auth.LoginRequest;
import br.com.fiap.agendamentoapi.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    @Transactional(readOnly = true)
    public String login(LoginRequest loginRequest) {
        log.info("Autenticando Usuário: {}", loginRequest.login());

        var usuario = usuarioRepository.findByLogin(loginRequest.login())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com login '%s' não encontrado!".formatted(loginRequest.login())));

        if (!passwordEncoder.matches(loginRequest.senha(), usuario.getSenha())) {
            throw new SenhaIncorretaException("A senha informada está incorreta!");
        }

        if (!estaAtivo(usuario)) {
            throw new UsuarioInativoException("O usuário com login '%s' está inativo!".formatted(loginRequest.login()));
        }

        log.info("Usuário autenticado com sucesso: {}", loginRequest.login());
        return tokenService.gerarToken(usuario);
    }

    private boolean estaAtivo(Usuario usuario) {
        return usuario.getSituacaoCadastro() != null
                && SituacaoCadastro.ATIVO.getId().equals(usuario.getSituacaoCadastro().getId());
    }
}
