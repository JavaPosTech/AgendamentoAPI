package br.com.fiap.agendamentoapi.service.agendamento;

import br.com.fiap.agendamentoapi.exceptions.ConsultaNaoEncontradaException;
import br.com.fiap.agendamentoapi.model.dto.agendamento.AgendamentoDTO;
import br.com.fiap.agendamentoapi.model.mapper.agendamento.AgendamentoMapper;
import br.com.fiap.agendamentoapi.model.request.agendamento.AtualizarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.repository.agendamento.AgendamentoRepository;
import br.com.fiap.agendamentoapi.service.medico.MedicoService;
import br.com.fiap.agendamentoapi.service.paciente.PacienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final MedicoService medicoService;

    private final PacienteService pacienteService;

    private final AgendamentoMapper agendamentoMapper;

    private final AgendamentoRepository agendamentoRepository;

    @Transactional(readOnly = true)
    public PageResponse<AgendamentoDTO> getAgendamentos(Pageable pageable) {
        log.info("Buscando informações de todas as consultas...");
        return PageResponse.from(agendamentoRepository.findAll(pageable), AgendamentoDTO::new);
    }

    @Transactional
    public MensagemSucessoResponse salvar(SalvarAgendamentoRequest salvarAgendamentoRequest) {
        var medico = medicoService.getMedicoById(salvarAgendamentoRequest.medicoId());
        var paciente = pacienteService.getPacienteById(salvarAgendamentoRequest.pacienteId());

        log.info("Salvando consulta... - Médico: [{}] - Paciente: [{}] - Data Consulta: [{}]",
                medico.getNome(),
                paciente.getNome(),
                salvarAgendamentoRequest.dataHoraConsulta());

        agendamentoRepository.save(agendamentoMapper.toEntity(salvarAgendamentoRequest, medico, paciente));
        return new MensagemSucessoResponse(201, "Consulta criada com sucesso!");
    }

    @Transactional
    public MensagemSucessoResponse atualizar(Integer id, AtualizarAgendamentoRequest atualizarAgendamentoRequest) {
        log.info("Atualizando Consulta... - ID: [{}]", id);
        var agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new ConsultaNaoEncontradaException("Consulta não encontrada!"));

        if (atualizarAgendamentoRequest.dataHoraConsulta() != null) {
            agendamento.setDataHoraConsulta(atualizarAgendamentoRequest.dataHoraConsulta());
        }

        if (atualizarAgendamentoRequest.observacao() != null) {
            agendamento.setObservacao(atualizarAgendamentoRequest.observacao());
        }

        return new MensagemSucessoResponse(200, "Consulta atualizada com sucesso!");
    }
}