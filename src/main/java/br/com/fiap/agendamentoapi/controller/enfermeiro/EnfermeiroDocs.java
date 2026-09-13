package br.com.fiap.agendamentoapi.controller.enfermeiro;

import br.com.fiap.agendamentoapi.controller.ParametrosPaginacao;
import br.com.fiap.agendamentoapi.exceptions.dto.ErrorResponseDTO;
import br.com.fiap.agendamentoapi.model.dto.enfermeiro.EnfermeiroDTO;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.AtualizarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.SalvarEnfermeiroRequest;
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

@Tag(name = "Enfermeiro", description = "Endpoints relacionados ao gerenciamento de Enfermeiros")
public interface EnfermeiroDocs {

    @ParametrosPaginacao
    @Operation(
            summary = "Lista os enfermeiros",
            description = """
                    Retorna os enfermeiros de forma paginada. Os cadastros excluídos continuam na lista, \
                    com a situação EXCLUIDO.

                    **Perfis com acesso:** ADMINISTRADOR, MEDICO, ENFERMEIRO e RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Enfermeiros retornados com sucesso!"),
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
    ResponseEntity<PageResponse<EnfermeiroDTO>> listarEnfermeiros(
            @Parameter(hidden = true)
            @PageableDefault(size = 100, sort = "id") Pageable pageable);

    @Operation(
            summary = "Cadastra um enfermeiro",
            description = """
                    Cadastra o enfermeiro e cria as credenciais de acesso dele com o perfil ENFERMEIRO. \
                    A senha é gravada criptografada e o cadastro começa com a situação ATIVO.

                    Login e COREN são únicos. Repetir um valor que já existe responde 409.

                    **Perfil com acesso:** ADMINISTRADOR.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Enfermeiro cadastrado com sucesso!"),
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
                    description = "Login ou COREN já cadastrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    ResponseEntity<MensagemSucessoResponse> salvarEnfermeiro(@RequestBody @Valid SalvarEnfermeiroRequest salvarEnfermeiroRequest);

    @Operation(
            summary = "Atualiza um enfermeiro",
            description = """
                    Atualiza parcialmente os dados do enfermeiro. Campos omitidos, nulos, vazios ou só \
                    com espaços mantêm o valor atual. Login, senha e situação do cadastro não mudam por esta rota.

                    **Perfil com acesso:** ADMINISTRADOR.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Enfermeiro atualizado com sucesso!"),
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
                    description = "Enfermeiro não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "COREN já cadastrado para outro enfermeiro!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/{id}")
    ResponseEntity<MensagemSucessoResponse> atualizarEnfermeiro(
            @Parameter(description = "Id do enfermeiro", example = "1")
            @PathVariable Integer id,

            @RequestBody @Valid AtualizarEnfermeiroRequest atualizarEnfermeiroRequest);

    @Operation(
            summary = "Exclui um enfermeiro",
            description = """
                    Exclui o enfermeiro de forma lógica. O registro continua no banco com a situação \
                    EXCLUIDO e as credenciais dele são desativadas: o login passa a responder 403 e os \
                    tokens emitidos antes da exclusão deixam de ser aceitos.

                    **Perfil com acesso:** ADMINISTRADOR.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Enfermeiro excluído com sucesso!"),
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
                    description = "Enfermeiro não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarEnfermeiro(
            @Parameter(description = "Id do enfermeiro", example = "1")
            @PathVariable Integer id);
}