package br.com.fiap.agendamentoapi.model.mapper.enfermeiro;

import br.com.fiap.agendamentoapi.model.entity.enfermeiro.Enfermeiro;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.AtualizarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.SalvarEnfermeiroRequest;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EnfermeiroMapper {

    @Condition
    default boolean preenchido(String valor) {
        return valor != null && !valor.isBlank();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    Enfermeiro toEntity(SalvarEnfermeiroRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    void updateEntity(AtualizarEnfermeiroRequest request, @MappingTarget Enfermeiro enfermeiro);

}