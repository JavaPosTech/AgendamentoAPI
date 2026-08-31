package br.com.fiap.agendamentoapi.model.mapper.recepcionista;

import br.com.fiap.agendamentoapi.model.entity.recepcionista.Recepcionista;
import br.com.fiap.agendamentoapi.model.request.recepcionista.AtualizarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.model.request.recepcionista.SalvarRecepcionistaRequest;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RecepcionistaMapper {

    @Condition
    default boolean preenchido(String valor) {
        return valor != null && !valor.isBlank();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    Recepcionista toEntity(SalvarRecepcionistaRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    void updateEntity(AtualizarRecepcionistaRequest request, @MappingTarget Recepcionista recepcionista);
}
