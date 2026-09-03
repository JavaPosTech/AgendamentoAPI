package br.com.fiap.agendamentoapi.service.agendamento;

import br.com.fiap.agendamentoapi.exceptions.ConsultaNaoEncontradaException;
import br.com.fiap.agendamentoapi.exceptions.HorarioConsultaIndisponivelException;
import br.com.fiap.agendamentoapi.exceptions.MedicoIndisponivelException;
import br.com.fiap.agendamentoapi.model.dto.agendamento.AgendamentoDTO;
import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import br.com.fiap.agendamentoapi.model.mapper.agendamento.AgendamentoMapper;
import br.com.fiap.agendamentoapi.model.request.agendamento.AtualizarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.repository.agendamento.AgendamentoRepository;
import br.com.fiap.agendamentoapi.repository.paciente.PacienteRepository;
import br.com.fiap.agendamentoapi.service.medico.MedicoService;
import br.com.fiap.agendamentoapi.service.paciente.PacienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final PacienteRepository pacienteRepository;

    private static final Duration INTERVALO_MINIMO_ENTRE_CONSULTAS = Duration.ofHours(1);

    @Transactional(readOnly = true)
    public PageResponse<AgendamentoDTO> getAgendamentos(Pageable pageable, Authentication authentication) {
        if (ehPaciente(authentication)) {
            log.info("Buscando consultas do paciente autenticado - Login: [{}]", authentication.getName());
            var pacienteId = pacienteRepository.findByUsuarioLogin(authentication.getName())
                    .map(Paciente::getId)
                    .orElse(null);
            return PageResponse.from(agendamentoRepository.findByPacienteId(pacienteId, pageable), AgendamentoDTO::new);
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

        validarHorarioDisponivel(salvarAgendamentoRequest.medicoId(), salvarAgendamentoRequest.pacienteId(), salvarAgendamentoRequest.dataHoraConsulta());

        log.info("Salvando consulta... - Médico: [{}] - Paciente: [{}] - Data Consulta: [{}]",
                medico.getNome(),
                paciente.getNome(),
                salvarAgendamentoRequest.dataHoraConsulta());

        validarDisponibilidadeDoMedico(medico.getId(), salvarAgendamentoRequest.dataHoraConsulta(), null);

        agendamentoRepository.save(agendamentoMapper.toEntity(salvarAgendamentoRequest, medico, paciente));
        return new MensagemSucessoResponse(201, "Consulta criada com sucesso!");
    }

    private void validarHorarioDisponivel(Integer medicoId, Integer pacienteId, LocalDateTime dataHoraConsulta) {
        if (agendamentoRepository.existsByMedicoIdAndDataHoraConsulta(medicoId, dataHoraConsulta)) {
            throw new HorarioConsultaIndisponivelException("O médico já possui uma consulta agendada para este horário!");
        }

        if (agendamentoRepository.existsByPacienteIdAndDataHoraConsulta(pacienteId, dataHoraConsulta)) {
            throw new HorarioConsultaIndisponivelException("O paciente já possui uma consulta agendada para este horário!");
        }
    }

    @Transactional
    public MensagemSucessoResponse atualizar(Integer id, AtualizarAgendamentoRequest atualizarAgendamentoRequest) {
        log.info("Atualizando Consulta... - ID: [{}]", id);
        var agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new ConsultaNaoEncontradaException("Consulta não encontrada!"));

        if (atualizarAgendamentoRequest.dataHoraConsulta() != null) {
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