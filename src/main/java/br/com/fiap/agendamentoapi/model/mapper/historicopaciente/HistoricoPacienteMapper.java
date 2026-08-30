package br.com.fiap.agendamentoapi.model.mapper.historicopaciente;

import br.com.fiap.agendamentoapi.model.entity.historicopaciente.HistoricoPaciente;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.AtualizarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.SalvarHistoricoPacienteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HistoricoPacienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    HistoricoPaciente toEntity(SalvarHistoricoPacienteRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    void updateEntity(AtualizarHistoricoPacienteRequest request, @MappingTarget HistoricoPaciente historicoPaciente);
}
