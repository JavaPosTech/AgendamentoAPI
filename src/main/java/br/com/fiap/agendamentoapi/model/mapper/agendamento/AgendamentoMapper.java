package br.com.fiap.agendamentoapi.model.mapper.agendamento;

import br.com.fiap.agendamentoapi.model.entity.agendamento.Agendamento;
import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import br.com.fiap.agendamentoapi.model.rabbitmq.AgendamentoAtualizadoEvent;
import br.com.fiap.agendamentoapi.model.rabbitmq.AgendamentoCriadoEvent;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medico", source = "medico")
    @Mapping(target = "paciente", source = "paciente")
    @Mapping(target = "dataHoraConsulta", source = "request.dataHoraConsulta")
    @Mapping(target = "observacao", source = "request.observacao")
    @Mapping(target = "dataCadastro", expression = "java(java.time.LocalDateTime.now())")
    Agendamento toEntity(SalvarAgendamentoRequest request, Medico medico, Paciente paciente);

    @Mapping(target = "eventId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "agendamentoId", source = "id")
    @Mapping(target = "pacienteId", source = "paciente.id")
    @Mapping(target = "pacienteNome", expression = "java(String.format(\"%s %s\", agendamento.getPaciente().getNome(), agendamento.getPaciente().getSobrenome()))")
    @Mapping(target = "pacienteEmail", source = "paciente.email")
    @Mapping(target = "medicoId", source = "medico.id")
    @Mapping(target = "medicoNome", expression = "java(String.format(\"%s %s\", agendamento.getMedico().getNome(), agendamento.getMedico().getSobrenome()))")
    @Mapping(target = "especialidade", source = "medico.especialidade")
    @Mapping(target = "dataHoraConsulta", source = "dataHoraConsulta")
    @Mapping(target = "observacao", source = "observacao")
    @Mapping(target = "ocorridoEm", expression = "java(java.time.LocalDateTime.now())")
    AgendamentoCriadoEvent toAgendamentoCriadoEvent(Agendamento agendamento);

    @Mapping(target = "eventId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "agendamentoId", source = "agendamento.id")
    @Mapping(target = "pacienteId", source = "agendamento.paciente.id")
    @Mapping(target = "pacienteNome", expression = "java(String.format(\"%s %s\", agendamento.getPaciente().getNome(), agendamento.getPaciente().getSobrenome()))")
    @Mapping(target = "pacienteEmail", source = "agendamento.paciente.email")
    @Mapping(target = "medicoId", source = "agendamento.medico.id")
    @Mapping(target = "medicoNome", expression = "java(String.format(\"%s %s\", agendamento.getMedico().getNome(), agendamento.getMedico().getSobrenome()))")
    @Mapping(target = "especialidade", source = "agendamento.medico.especialidade")
    @Mapping(target = "dataHoraAnterior", source = "dataHoraAnterior")
    @Mapping(target = "dataHoraAtual", source = "agendamento.dataHoraConsulta")
    @Mapping(target = "observacao", source = "agendamento.observacao")
    @Mapping(target = "ocorridoEm", expression = "java(java.time.LocalDateTime.now())")
    AgendamentoAtualizadoEvent toAgendamentoAtualizadoEvent(Agendamento agendamento, LocalDateTime dataHoraAnterior);


}