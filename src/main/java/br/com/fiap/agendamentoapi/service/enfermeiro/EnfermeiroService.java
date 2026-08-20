package br.com.fiap.agendamentoapi.service.enfermeiro;

import br.com.fiap.agendamentoapi.enums.SituacaoCadastro;
import br.com.fiap.agendamentoapi.enums.TipoUsuario;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.dto.enfermeiro.EnfermeiroDTO;
import br.com.fiap.agendamentoapi.model.dto.usuario.UsuarioDTO;
import br.com.fiap.agendamentoapi.model.mapper.enfermeiro.EnfermeiroMapper;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.AtualizarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.CriarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.repository.enfermeiro.EnfermeiroRepository;
import br.com.fiap.agendamentoapi.service.situacaocadastro.SituacaoCadastroService;
import br.com.fiap.agendamentoapi.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnfermeiroService {

    private final UsuarioService usuarioService;

    private final EnfermeiroMapper enfermeiroMapper;

    private final EnfermeiroRepository enfermeiroRepository;

    private final SituacaoCadastroService situacaoCadastroService;

    @Transactional(readOnly = true)
    public PageResponse<EnfermeiroDTO> getEnfermeiros(Pageable pageable) {
        log.info("Buscando informações de todos os Enfermeiros...");
        return PageResponse.from(enfermeiroRepository.findAll(pageable), EnfermeiroDTO::new);
    }

    @Transactional
    public MensagemSucessoResponse salvar(CriarEnfermeiroRequest criarEnfermeiroRequest) {
        log.info("Salvando Enfermeiro... - Nome: {}", criarEnfermeiroRequest.nome());

        var usuarioId = usuarioService.salvar(new UsuarioDTO(
                criarEnfermeiroRequest.login(),
                criarEnfermeiroRequest.senha(),
                TipoUsuario.ENFERMEIRO.getId()));

        var enfermeiro = enfermeiroMapper.toEntity(criarEnfermeiroRequest);
        enfermeiro.setDataCadastro(LocalDateTime.now());
        enfermeiro.setUsuario(usuarioService.buscarReferenciaPorId(usuarioId));
        enfermeiro.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.ATIVO.getId()));

        enfermeiroRepository.save(enfermeiro);
        log.info("Enfermeiro salvo com sucesso! - Nome: {}", criarEnfermeiroRequest.nome());
        return new MensagemSucessoResponse(201, "Enfermeiro criado com sucesso!");
    }

    @Transactional
    public MensagemSucessoResponse atualizar(Integer id, AtualizarEnfermeiroRequest atualizarEnfermeiroRequest) {
        log.info("Atualizando Enfermeiro... - ID: {}", id);
        var enfermeiro = enfermeiroRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Enfermeiro não encontrado!"));

        enfermeiroMapper.updateEntity(atualizarEnfermeiroRequest, enfermeiro);
        log.info("Enfermeiro atualizado com sucesso! - ID: [{}]", id);
        return new MensagemSucessoResponse(200, "Enfermeiro atualizado com sucesso!");
    }

    @Transactional
    public void deletar(Integer id) {
        log.info("Excluindo Enfermeiro... - ID: [{}]", id);
        var enfermeiro = enfermeiroRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Enfermeiro não encontrado!"));

        enfermeiro.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.EXCLUIDO.getId()));
        log.info("Enfermeiro excluído com sucesso! - ID: [{}]", id);
    }
}