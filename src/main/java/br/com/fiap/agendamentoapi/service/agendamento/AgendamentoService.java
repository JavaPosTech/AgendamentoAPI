package br.com.fiap.agendamentoapi.service.agendamento;

import br.com.fiap.agendamentoapi.exceptions.ConsultaNaoEncontradaException;
import br.com.fiap.agendamentoapi.exceptions.HorarioConsultaIndisponivelException;
import br.com.fiap.agendamentoapi.exceptions.MedicoIndisponivelException;
import br.com.fiap.agendamentoapi.model.dto.agendamento.AgendamentoDTO;
import br.com.fiap.agendamentoapi.model.entity.agendamento.Agendamento;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final MedicoService medicoService;

    private final PacienteService pacienteService;

    private final AgendamentoMapper agendamentoMapper;

    private final AgendamentoRepository agendamentoRepository;

    private static final Duration INTERVALO_MINIMO_ENTRE_CONSULTAS = Duration.ofHours(1);

    @Transactional(readOnly = true)
    public PageResponse<AgendamentoDTO> getAgendamentos(Pageable pageable, Authentication authentication) {
        if (ehPaciente(authentication)) {
            log.info("Buscando consultas do paciente autenticado - Login: [{}]", authentication.getName());
            return pacienteService.getPacienteByLogin(authentication.getName())
                    .map(paciente -> PageResponse.from(agendamentoRepository.findByPacienteId(paciente.getId(), pageable), AgendamentoDTO::new))
                    .orElseGet(() -> PageResponse.from(Page.<Agendamento>empty(pageable), AgendamentoDTO::new));
        }

        log.info("Buscando informações de todas as consultas...");
        return PageResponse.from(agendamentoRepository.findAll(pageable), AgendamentoDTO::new);
    }

    private boolean ehPaciente(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_PACIENTE".equals(authority.getAuthority()));
    }

    @Transactional
    public MensagemSucessoResponse salvar(SalvarAgendamentoRequest salvarAgendamentoRequest) {
        var medico = medicoService.getMedicoById(salvarAgendamentoRequest.medicoId());
        var paciente = pacienteService.getPacienteById(salvarAgendamentoRequest.pacienteId());

        validarDisponibilidadeDoPaciente(paciente.getId(), salvarAgendamentoRequest.dataHoraConsulta(), null);
        validarDisponibilidadeDoMedico(medico.getId(), salvarAgendamentoRequest.dataHoraConsulta(), null);

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
            validarDisponibilidadeDoPaciente(agendamento.getPaciente().getId(), atualizarAgendamentoRequest.dataHoraConsulta(), agendamento.getId());
            validarDisponibilidadeDoMedico(agendamento.getMedico().getId(), atualizarAgendamentoRequest.dataHoraConsulta(), agendamento.getId());
            agendamento.setDataHoraConsulta(atualizarAgendamentoRequest.dataHoraConsulta());
        }

        if (atualizarAgendamentoRequest.observacao() != null) {
            agendamento.setObservacao(atualizarAgendamentoRequest.observacao());
        }

        return new MensagemSucessoResponse(200, "Consulta atualizada com sucesso!");
    }

    @Transactional
    public void cancelar(Integer id) {
        log.info("Cancelando Consulta... - ID: [{}]", id);
        var agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new ConsultaNaoEncontradaException("Consulta não encontrada!"));

        agendamentoRepository.delete(agendamento);
        log.info("Consulta cancelada com sucesso! - ID: [{}]", id);
    }

    private void validarDisponibilidadeDoPaciente(Integer pacienteId, LocalDateTime dataHoraConsulta, Integer agendamentoId) {
        var indisponivel = agendamentoId == null
                ? agendamentoRepository.existsByPacienteIdAndDataHoraConsulta(pacienteId, dataHoraConsulta)
                : agendamentoRepository.existsByPacienteIdAndIdNotAndDataHoraConsulta(pacienteId, agendamentoId, dataHoraConsulta);

        if (indisponivel) {
            log.warn("Horário indisponível para o Paciente! - Paciente: [ID: {}] - Data Consulta: [{}]", pacienteId, dataHoraConsulta);
            throw new HorarioConsultaIndisponivelException("O paciente já possui uma consulta agendada para este horário!");
        }
    }

    private void validarDisponibilidadeDoMedico(Integer medicoId, LocalDateTime dataHoraConsulta, Integer agendamentoId) {
        var fimIntervalo = dataHoraConsulta.plus(INTERVALO_MINIMO_ENTRE_CONSULTAS);
        var inicioIntervalo = dataHoraConsulta.minus(INTERVALO_MINIMO_ENTRE_CONSULTAS);

        var indisponivel = agendamentoId == null
                ? agendamentoRepository.existsByMedicoIdAndDataHoraConsultaAfterAndDataHoraConsultaBefore(medicoId, inicioIntervalo, fimIntervalo)
                : agendamentoRepository.existsByMedicoIdAndIdNotAndDataHoraConsultaAfterAndDataHoraConsultaBefore(medicoId, agendamentoId, inicioIntervalo, fimIntervalo);

        if (indisponivel) {
            log.warn("Horário indisponível para o Médico! - Médico: [ID: {}] - Data Consulta: [{}]", medicoId, dataHoraConsulta);
            throw new MedicoIndisponivelException("O médico já possui uma consulta agendada nesse horário! É necessário um intervalo mínimo de 1 hora entre as consultas.");
        }
    }
}
