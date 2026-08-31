package br.com.fiap.agendamentoapi.service.medico;

import br.com.fiap.agendamentoapi.enums.SituacaoCadastro;
import br.com.fiap.agendamentoapi.enums.TipoUsuario;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.dto.medico.MedicoDTO;
import br.com.fiap.agendamentoapi.model.dto.usuario.UsuarioDTO;
import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import br.com.fiap.agendamentoapi.model.mapper.medico.MedicoMapper;
import br.com.fiap.agendamentoapi.model.request.medico.AtualizarMedicoRequest;
import br.com.fiap.agendamentoapi.model.request.medico.SalvarMedicoRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.repository.medico.MedicoRepository;
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
public class MedicoService {

    private final MedicoMapper medicoMapper;

    private final UsuarioService usuarioService;

    private final MedicoRepository medicoRepository;

    private final SituacaoCadastroService situacaoCadastroService;

    @Transactional(readOnly = true)
    public PageResponse<MedicoDTO> getMedicos(Pageable pageable) {
        log.info("Buscando informações de todos os Médicos...");
        return PageResponse.from(medicoRepository.findAll(pageable), MedicoDTO::new);
    }

    public Medico getMedicoById(Integer id) {
        log.info("Buscando informações do Médico - ID: [{}]", id);
        return medicoRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Médico não encontrado!"));
    }

    @Transactional
    public MensagemSucessoResponse salvar(SalvarMedicoRequest salvarMedicoRequest) {
        log.info("Salvando Médico... - Nome: {}", salvarMedicoRequest.nome());

        var usuarioId = usuarioService.salvar(new UsuarioDTO(
                salvarMedicoRequest.login(),
                salvarMedicoRequest.senha(),
                TipoUsuario.MEDICO.getId()));

        var medico = medicoMapper.toEntity(salvarMedicoRequest);
        medico.setDataCadastro(LocalDateTime.now());
        medico.setUsuario(usuarioService.buscarReferenciaPorId(usuarioId));
        medico.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.ATIVO.getId()));

        medicoRepository.save(medico);
        log.info("Médico salvo com sucesso! - Nome: {}", salvarMedicoRequest.nome());
        return new MensagemSucessoResponse(201, "Médico criado com sucesso!");
    }

    @Transactional
    public MensagemSucessoResponse atualizar(Integer id, AtualizarMedicoRequest atualizarMedicoRequest) {
        log.info("Atualizando Médico... - ID: {}", id);
        var medico = medicoRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Médico não encontrado!"));

        medicoMapper.updateEntity(atualizarMedicoRequest, medico);
        log.info("Médico atualizado com sucesso! - ID: [{}]", id);
        return new MensagemSucessoResponse(200, "Médico atualizado com sucesso!");
    }

    @Transactional
    public void deletar(Integer id) {
        log.info("Excluindo Médico... - ID: [{}]", id);
        var medico = medicoRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Médico não encontrado!"));

        medico.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.EXCLUIDO.getId()));
        usuarioService.desativar(medico.getUsuario());
        log.info("Médico excluído com sucesso! - ID: [{}]", id);
    }
}