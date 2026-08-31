package br.com.fiap.agendamentoapi.model.dto.auth;

import br.com.fiap.agendamentoapi.enums.SituacaoCadastro;
import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioDetailsImpl implements UserDetails {

    @Getter
    private final Usuario usuario;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean ativo;

    public UsuarioDetailsImpl(Usuario usuario, String tipoUsuarioDescricao) {
        this.usuario = usuario;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuarioDescricao));
        this.ativo = usuario.getSituacaoCadastro() != null
                && SituacaoCadastro.ATIVO.getId().equals(usuario.getSituacaoCadastro().getId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
