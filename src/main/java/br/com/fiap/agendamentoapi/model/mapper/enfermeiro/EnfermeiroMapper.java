package br.com.fiap.agendamentoapi.model.mapper.enfermeiro;

import br.com.fiap.agendamentoapi.model.entity.enfermeiro.Enfermeiro;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.AtualizarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.CriarEnfermeiroRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnfermeiroMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    Enfermeiro toEntity(CriarEnfermeiroRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    void updateEntity(AtualizarEnfermeiroRequest request, @MappingTarget Enfermeiro enfermeiro);

}