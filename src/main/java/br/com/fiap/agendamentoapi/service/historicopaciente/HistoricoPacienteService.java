package br.com.fiap.agendamentoapi.service.historicopaciente;

import br.com.fiap.agendamentoapi.exceptions.HistoricoPacienteNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.dto.historicopaciente.HistoricoPacienteDTO;
import br.com.fiap.agendamentoapi.model.mapper.historicopaciente.HistoricoPacienteMapper;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.AtualizarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.SalvarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.repository.historicopaciente.HistoricoPacienteRepository;
import br.com.fiap.agendamentoapi.service.paciente.PacienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoricoPacienteService {

    private final PacienteService pacienteService;

    private final HistoricoPacienteMapper historicoPacienteMapper;

    private final HistoricoPacienteRepository historicoPacienteRepository;

    @Transactional(readOnly = true)
    public PageResponse<HistoricoPacienteDTO> getHistoricos(Pageable pageable) {
        log.info("Buscando históricos dos Pacientes...");
        return PageResponse.from(historicoPacienteRepository.findAll(pageable), HistoricoPacienteDTO::new);
    }

    @Transactional
    public MensagemSucessoResponse salvar(SalvarHistoricoPacienteRequest salvarHistoricoPacienteRequest) {
        var paciente = pacienteService.getPacienteById(salvarHistoricoPacienteRequest.pacienteId());

        log.info("Salvando Histórico do Paciente... - Paciente ID: [{}]", paciente.getId());

        var historicoPaciente = historicoPacienteMapper.toEntity(salvarHistoricoPacienteRequest);
        historicoPaciente.setPaciente(paciente);
        historicoPacienteRepository.save(historicoPaciente);

        log.info("Histórico do Paciente salvo com sucesso! - Paciente ID: [{}]", paciente.getId());
        return new MensagemSucessoResponse(201, "Histórico do paciente criado com sucesso!");
    }

    @Transactional
    public MensagemSucessoResponse atualizar(Integer id, AtualizarHistoricoPacienteRequest atualizarHistoricoPacienteRequest) {
        log.info("Atualizando Histórico do Paciente... - ID: [{}]", id);
        var historicoPaciente = historicoPacienteRepository.findById(id)
                .orElseThrow(() -> new HistoricoPacienteNaoEncontradoException("Histórico do paciente não encontrado!"));

        historicoPacienteMapper.updateEntity(atualizarHistoricoPacienteRequest, historicoPaciente);
        log.info("Histórico do Paciente atualizado com sucesso! - ID: [{}]", id);
        return new MensagemSucessoResponse(200, "Histórico do paciente atualizado com sucesso!");
    }

    @Transactional
    public void deletar(Integer id) {
        log.info("Excluindo Histórico do Paciente... - ID: [{}]", id);
        var historicoPaciente = historicoPacienteRepository.findById(id)
                .orElseThrow(() -> new HistoricoPacienteNaoEncontradoException("Histórico do paciente não encontrado!"));

        historicoPacienteRepository.delete(historicoPaciente);
        log.info("Histórico do Paciente excluído com sucesso! - ID: [{}]", id);
    }
}
