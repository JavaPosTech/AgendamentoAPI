package br.com.fiap.agendamentoapi.controller.medico;

import br.com.fiap.agendamentoapi.controller.ParametrosPaginacao;
import br.com.fiap.agendamentoapi.exceptions.dto.ErrorResponseDTO;
import br.com.fiap.agendamentoapi.model.dto.medico.MedicoDTO;
import br.com.fiap.agendamentoapi.model.request.medico.AtualizarMedicoRequest;
import br.com.fiap.agendamentoapi.model.request.medico.SalvarMedicoRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Médico", description = "Endpoints relacionados ao gerenciamento de Médicos")
public interface MedicoDocs {

    @ParametrosPaginacao
    @Operation(
            summary = "Lista os médicos",
            description = """
                    Retorna os médicos de forma paginada. Os cadastros excluídos continuam na lista, \
                    com a situação EXCLUIDO.

                    **Perfis com acesso:** ADMINISTRADOR, MEDICO, ENFERMEIRO e RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Médicos retornados com sucesso!"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token de acesso ausente, inválido ou expirado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "O perfil do usuário não tem acesso a este recurso!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    ResponseEntity<PageResponse<MedicoDTO>> listarMedicos(
            @Parameter(hidden = true)
            @PageableDefault(size = 100, sort = "id") Pageable pageable);

    @Operation(
            summary = "Cadastra um médico",
            description = """
                    Cadastra o médico e cria as credenciais de acesso dele com o perfil MEDICO. A senha \
                    é gravada criptografada e o cadastro começa com a situação ATIVO.

                    Login e CRM são únicos. Repetir um valor que já existe responde 409.

                    **Perfil com acesso:** ADMINISTRADOR.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Médico cadastrado com sucesso!"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos na requisição!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token de acesso ausente, inválido ou expirado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "O perfil do usuário não tem acesso a este recurso!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Login ou CRM já cadastrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    ResponseEntity<MensagemSucessoResponse> salvarMedico(@RequestBody @Valid SalvarMedicoRequest salvarMedicoRequest);

    @Operation(
            summary = "Atualiza um médico",
            description = """
                    Atualiza parcialmente os dados do médico. Campos omitidos, nulos, vazios ou só com \
                    espaços mantêm o valor atual. Login, senha e situação do cadastro não mudam por esta rota.

                    **Perfil com acesso:** ADMINISTRADOR.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Médico atualizado com sucesso!"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos na requisição!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token de acesso ausente, inválido ou expirado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "O perfil do usuário não tem acesso a este recurso!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Médico não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "CRM já cadastrado para outro médico!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/{id}")
    ResponseEntity<MensagemSucessoResponse> atualizarMedico(
            @Parameter(description = "Id do médico", example = "1")
            @PathVariable Integer id,

            @RequestBody @Valid AtualizarMedicoRequest atualizarMedicoRequest);

    @Operation(
            summary = "Exclui um médico",
            description = """
                    Exclui o médico de forma lógica. O registro continua no banco com a situação EXCLUIDO \
                    e as credenciais dele são desativadas: o login passa a responder 403 e os tokens \
                    emitidos antes da exclusão deixam de ser aceitos.

                    **Perfil com acesso:** ADMINISTRADOR.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Médico excluído com sucesso!"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token de acesso ausente, inválido ou expirado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "O perfil do usuário não tem acesso a este recurso!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Médico não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarMedico(
            @Parameter(description = "Id do médico", example = "1")
            @PathVariable Integer id);
}