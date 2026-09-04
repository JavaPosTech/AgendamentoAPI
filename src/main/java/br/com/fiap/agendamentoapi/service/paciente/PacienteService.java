package br.com.fiap.agendamentoapi.service.paciente;

import br.com.fiap.agendamentoapi.enums.SituacaoCadastro;
import br.com.fiap.agendamentoapi.enums.TipoUsuario;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.dto.paciente.PacienteDTO;
import br.com.fiap.agendamentoapi.model.dto.usuario.UsuarioDTO;
import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import br.com.fiap.agendamentoapi.model.mapper.paciente.PacienteMapper;
import br.com.fiap.agendamentoapi.model.request.paciente.AtualizarPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.paciente.SalvarPacienteRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.repository.paciente.PacienteRepository;
import br.com.fiap.agendamentoapi.service.situacaocadastro.SituacaoCadastroService;
import br.com.fiap.agendamentoapi.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final UsuarioService usuarioService;

    private final PacienteMapper pacienteMapper;

    private final PacienteRepository pacienteRepository;

    private final SituacaoCadastroService situacaoCadastroService;

    @Transactional(readOnly = true)
    public PageResponse<PacienteDTO> getPacientes(Pageable pageable) {
        log.info("Buscando informações de todos os Pacientes...");
        return PageResponse.from(pacienteRepository.findAll(pageable), PacienteDTO::new);
    }

    public Paciente getPacienteById(Integer id) {
        log.info("Buscando informações do Paciente - ID: [{}]", id);
        return pacienteRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Paciente não encontrado!"));
    }

    @Transactional(readOnly = true)
    public Optional<Paciente> getPacienteByLogin(String login) {
        log.info("Buscando informações do Paciente - Login: [{}]", login);
        return pacienteRepository.findByUsuarioLogin(login);
    }

    @Transactional
    public MensagemSucessoResponse salvar(SalvarPacienteRequest salvarPacienteRequest) {
        log.info("Salvando Paciente... - Nome: {}", salvarPacienteRequest.nome());

        var usuarioId = usuarioService.salvar(new UsuarioDTO(
                salvarPacienteRequest.login(),
                salvarPacienteRequest.senha(),
                TipoUsuario.PACIENTE.getId()));

        var paciente = pacienteMapper.toEntity(salvarPacienteRequest);
        paciente.setDataCadastro(LocalDateTime.now());
        paciente.setUsuario(usuarioService.buscarReferenciaPorId(usuarioId));
        paciente.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.ATIVO.getId()));

        pacienteRepository.save(paciente);
        log.info("Paciente salvo com sucesso! - Nome: {}", salvarPacienteRequest.nome());
        return new MensagemSucessoResponse(201, "Paciente criado com sucesso!");
    }

    @Transactional
    public MensagemSucessoResponse atualizar(Integer id, AtualizarPacienteRequest atualizarPacienteRequest) {
        log.info("Atualizando Paciente... - ID: [{}]", id);
        var paciente = pacienteRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Paciente não encontrado!"));

        pacienteMapper.updateEntity(atualizarPacienteRequest, paciente);
        log.info("Paciente atualizado com sucesso! - ID: [{}]", id);
        return new MensagemSucessoResponse(200, "Paciente atualizado com sucesso!");
    }

    @Transactional
    public void deletar(Integer id) {
        log.info("Excluindo Paciente... - ID: [{}]", id);
        var paciente = pacienteRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Paciente não encontrado!"));

        paciente.setSituacaoCadastro(situacaoCadastroService.buscarReferenciaPorId(SituacaoCadastro.EXCLUIDO.getId()));
        usuarioService.desativar(paciente.getUsuario());
        log.info("Paciente excluído com sucesso! - ID: [{}]", id);
    }
}