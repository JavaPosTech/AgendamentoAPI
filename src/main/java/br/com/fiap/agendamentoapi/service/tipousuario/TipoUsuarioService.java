package br.com.fiap.agendamentoapi.service.tipousuario;

import br.com.fiap.agendamentoapi.exceptions.TipoUsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.entity.tipousuario.TipoUsuario;
import br.com.fiap.agendamentoapi.repository.tipousuario.TipoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    @Transactional(readOnly = true)
    public TipoUsuario buscarPorId(Integer id) {
        log.info("Buscando Tipo de Usuário com ID: [{}]", id);

        return tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException("Tipo de Usuário não encontrado!"));
    }
}