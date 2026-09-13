package br.com.fiap.agendamentoapi.controller.paciente;

import br.com.fiap.agendamentoapi.controller.ParametrosPaginacao;
import br.com.fiap.agendamentoapi.exceptions.dto.ErrorResponseDTO;
import br.com.fiap.agendamentoapi.model.dto.paciente.PacienteDTO;
import br.com.fiap.agendamentoapi.model.request.paciente.AtualizarPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.paciente.SalvarPacienteRequest;
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

@Tag(name = "Paciente", description = "Endpoints relacionados ao gerenciamento de Pacientes")
public interface PacienteDocs {

    @ParametrosPaginacao
    @Operation(
            summary = "Lista os pacientes",
            description = """
                    Retorna os pacientes de forma paginada. Os cadastros excluídos continuam na lista, \
                    com a situação EXCLUIDO.

                    **Perfis com acesso:** ADMINISTRADOR, MEDICO, ENFERMEIRO e RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pacientes retornados com sucesso!"),
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
    ResponseEntity<PageResponse<PacienteDTO>> listarPacientes(
            @Parameter(hidden = true)
            @PageableDefault(size = 100, sort = "id") Pageable pageable);

    @Operation(
            summary = "Cadastra um paciente",
            description = """
                    Cadastra o paciente e cria as credenciais de acesso dele com o perfil PACIENTE. \
                    A senha é gravada criptografada e o cadastro começa com a situação ATIVO. Com essas \
                    credenciais o paciente consegue consultar as próprias consultas.

                    Login, CPF e e-mail são únicos. Repetir um valor que já existe responde 409.

                    **Perfil com acesso:** RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Paciente cadastrado com sucesso!"),
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
                    description = "Login, CPF ou e-mail já cadastrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    ResponseEntity<MensagemSucessoResponse> salvarPaciente(@RequestBody @Valid SalvarPacienteRequest salvarPacienteRequest);

    @Operation(
            summary = "Atualiza um paciente",
            description = """
                    Atualiza parcialmente os dados do paciente. Campos omitidos, nulos, vazios ou só com \
                    espaços mantêm o valor atual. As validações de formato de CPF, e-mail e telefone valem \
                    apenas para os campos enviados preenchidos. Login, senha e situação do cadastro não \
                    mudam por esta rota.

                    **Perfis com acesso:** ADMINISTRADOR, MEDICO, ENFERMEIRO e RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente atualizado com sucesso!"),
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
                    description = "Paciente não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "CPF ou e-mail já cadastrado para outro paciente!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/{id}")
    ResponseEntity<MensagemSucessoResponse> atualizarPaciente(
            @Parameter(description = "Id do paciente", example = "1")
            @PathVariable Integer id,

            @RequestBody @Valid AtualizarPacienteRequest atualizarPacienteRequest);

    @Operation(
            summary = "Exclui um paciente",
            description = """
                    Exclui o paciente de forma lógica. O registro continua no banco com a situação \
                    EXCLUIDO e as credenciais dele são desativadas: o login passa a responder 403 e os \
                    tokens emitidos antes da exclusão deixam de ser aceitos.

                    **Perfil com acesso:** ADMINISTRADOR.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Paciente excluído com sucesso!"),
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
                    description = "Paciente não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarPaciente(
            @Parameter(description = "Id do paciente", example = "1")
            @PathVariable Integer id);
}