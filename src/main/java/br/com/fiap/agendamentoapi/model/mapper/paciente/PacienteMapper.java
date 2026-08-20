package br.com.fiap.agendamentoapi.model.mapper.paciente;

import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import br.com.fiap.agendamentoapi.model.request.AtualizarPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.CriarPacienteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    Paciente toEntity(CriarPacienteRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    void updateEntity(AtualizarPacienteRequest request, @MappingTarget Paciente paciente);
}