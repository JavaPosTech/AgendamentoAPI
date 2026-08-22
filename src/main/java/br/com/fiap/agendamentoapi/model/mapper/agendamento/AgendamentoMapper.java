package br.com.fiap.agendamentoapi.model.mapper.agendamento;

import br.com.fiap.agendamentoapi.model.entity.agendamento.Agendamento;
import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medico", source = "medico")
    @Mapping(target = "paciente", source = "paciente")
    @Mapping(target = "dataHoraConsulta", source = "request.dataHoraConsulta")
    @Mapping(target = "dataCadastro", expression = "java(java.time.LocalDateTime.now())")
    Agendamento toEntity(SalvarAgendamentoRequest request, Medico medico, Paciente paciente);

}