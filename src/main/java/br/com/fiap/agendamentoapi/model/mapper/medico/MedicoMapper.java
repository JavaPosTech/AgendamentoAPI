package br.com.fiap.agendamentoapi.model.mapper.medico;

import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import br.com.fiap.agendamentoapi.model.request.medico.AtualizarMedicoRequest;
import br.com.fiap.agendamentoapi.model.request.medico.CriarMedicoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    Medico toEntity(CriarMedicoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    void updateEntity(AtualizarMedicoRequest request, @MappingTarget Medico medico);

}