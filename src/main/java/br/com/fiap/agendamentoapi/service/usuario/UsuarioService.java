package br.com.fiap.agendamentoapi.service.usuario;

import br.com.fiap.agendamentoapi.enums.SituacaoCadastro;
import br.com.fiap.agendamentoapi.model.dto.usuario.UsuarioDTO;
import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import br.com.fiap.agendamentoapi.model.mapper.usuario.UsuarioMapper;
import br.com.fiap.agendamentoapi.repository.usuario.UsuarioRepository;
import br.com.fiap.agendamentoapi.service.situacaocadastro.SituacaoCadastroService;
import br.com.fiap.agendamentoapi.service.tipousuario.TipoUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;

    private final UsuarioRepository usuarioRepository;

    private final TipoUsuarioService tipoUsuarioService;

    private final SituacaoCadastroService situacaoCadastroService;

    public Integer salvar(UsuarioDTO usuarioDTO) {
        log.info("Salvando credenciais do Usuário: {}", usuarioDTO.login());

        var usuario = processarUsuario(usuarioDTO);
        var usuarioSalvo = usuarioRepository.save(usuario);

        log.info("Credenciais do Usuário salvas com sucesso! ID: [{}]", usuarioSalvo.getId());
        return usuarioSalvo.getId();
    }

    @Transactional(readOnly = true)
    public Usuario buscarReferenciaPorId(Integer id) {
        log.info("Buscando Usuário com ID: [{}]", id);
        return usuarioRepository.getReferenceById(id);
    }

    @Transactional
    public void desativar(Usuario usuario) {
        log.info("Desativando credenciais do Usuário - ID: [{}]", usuario.getId());
        usuario.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.EXCLUIDO.getId()));
        log.info("Credenciais do Usuário desativadas com sucesso! - ID: [{}]", usuario.getId());
    }

    private Usuario processarUsuario(UsuarioDTO usuarioDTO) {
        log.info("Processando credenciais do Usuário...");
        var usuario = usuarioMapper.toEntity(usuarioDTO);

        usuario.setSenha(encriptografarSenha(usuarioDTO.senha()));
        usuario.setTipoUsuario(tipoUsuarioService.buscarPorId(usuarioDTO.tipoUsuarioId()));
        usuario.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.ATIVO.getId()));

        return usuario;
    }

    private String encriptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }
}
