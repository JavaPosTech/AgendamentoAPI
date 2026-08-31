package br.com.fiap.agendamentoapi.service.recepcionista;

import br.com.fiap.agendamentoapi.enums.SituacaoCadastro;
import br.com.fiap.agendamentoapi.enums.TipoUsuario;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.dto.recepcionista.RecepcionistaDTO;
import br.com.fiap.agendamentoapi.model.dto.usuario.UsuarioDTO;
import br.com.fiap.agendamentoapi.model.mapper.recepcionista.RecepcionistaMapper;
import br.com.fiap.agendamentoapi.model.request.recepcionista.AtualizarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.model.request.recepcionista.SalvarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.repository.recepcionista.RecepcionistaRepository;
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
public class RecepcionistaService {

    private final UsuarioService usuarioService;

    private final RecepcionistaMapper recepcionistaMapper;

    private final RecepcionistaRepository recepcionistaRepository;

    private final SituacaoCadastroService situacaoCadastroService;

    @Transactional(readOnly = true)
    public PageResponse<RecepcionistaDTO> getRecepcionistas(Pageable pageable) {
        log.info("Buscando informações de todos os Recepcionistas...");
        return PageResponse.from(recepcionistaRepository.findAll(pageable), RecepcionistaDTO::new);
    }

    @Transactional
    public MensagemSucessoResponse salvar(SalvarRecepcionistaRequest salvarRecepcionistaRequest) {
        log.info("Salvando Recepcionista... - Nome: {}", salvarRecepcionistaRequest.nome());

        var usuarioId = usuarioService.salvar(new UsuarioDTO(
                salvarRecepcionistaRequest.login(),
                salvarRecepcionistaRequest.senha(),
                TipoUsuario.RECEPCIONISTA.getId()));

        var recepcionista = recepcionistaMapper.toEntity(salvarRecepcionistaRequest);
        recepcionista.setDataCadastro(LocalDateTime.now());
        recepcionista.setUsuario(usuarioService.buscarReferenciaPorId(usuarioId));
        recepcionista.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.ATIVO.getId()));

        recepcionistaRepository.save(recepcionista);
        log.info("Recepcionista salvo com sucesso! - Nome: {}", salvarRecepcionistaRequest.nome());
        return new MensagemSucessoResponse(201, "Recepcionista criado com sucesso!");
    }

    @Transactional
    public MensagemSucessoResponse atualizar(Integer id, AtualizarRecepcionistaRequest atualizarRecepcionistaRequest) {
        log.info("Atualizando Recepcionista... - ID: [{}]", id);
        var recepcionista = recepcionistaRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Recepcionista não encontrado!"));

        recepcionistaMapper.updateEntity(atualizarRecepcionistaRequest, recepcionista);
        log.info("Recepcionista atualizado com sucesso! - ID: [{}]", id);
        return new MensagemSucessoResponse(200, "Recepcionista atualizado com sucesso!");
    }

    @Transactional
    public void deletar(Integer id) {
        log.info("Excluindo Recepcionista... - ID: [{}]", id);
        var recepcionista = recepcionistaRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Recepcionista não encontrado!"));

        recepcionista.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.EXCLUIDO.getId()));
        usuarioService.desativar(recepcionista.getUsuario());
        log.info("Recepcionista excluído com sucesso! - ID: [{}]", id);
    }
}
