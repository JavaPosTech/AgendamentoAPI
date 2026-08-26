package br.com.fiap.agendamentoapi.service.auth;

import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.dto.auth.UsuarioDetailsImpl;
import br.com.fiap.agendamentoapi.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String login) {
        var usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com login '%s' não encontrado!".formatted(login)));

        return new UsuarioDetailsImpl(usuario, usuario.getTipoUsuario().getDescricao());
    }
}