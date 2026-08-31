package br.com.fiap.agendamentoapi.model.mapper.usuario;

import br.com.fiap.agendamentoapi.model.dto.usuario.UsuarioDTO;
import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "tipoUsuarioId", source = "tipoUsuario.id")
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoUsuario", ignore = true)
    @Mapping(target = "situacaoCadastro", ignore = true)
    Usuario toEntity(UsuarioDTO dto);

}
