package br.com.fiap.agendamentoapi.model.mapper.paciente;

import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import br.com.fiap.agendamentoapi.model.request.paciente.AtualizarPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.paciente.SalvarPacienteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PacienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    Paciente toEntity(SalvarPacienteRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    void updateEntity(AtualizarPacienteRequest request, @MappingTarget Paciente paciente);
}