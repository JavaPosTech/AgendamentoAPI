package br.com.fiap.agendamentoapi.controller.agendamento;

import br.com.fiap.agendamentoapi.controller.ParametrosPaginacao;
import br.com.fiap.agendamentoapi.exceptions.dto.ErrorResponseDTO;
import br.com.fiap.agendamentoapi.model.dto.agendamento.AgendamentoDTO;
import br.com.fiap.agendamentoapi.model.request.agendamento.AtualizarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agendamento", description = "Endpoints relacionados ao agendamento de Consultas")
public interface AgendamentoDocs {

    @ParametrosPaginacao
    @Operation(
            summary = "Lista as consultas",
            description = """
                    Retorna as consultas de forma paginada. O resultado depende do perfil de quem chama: \
                    MEDICO, RECEPCIONISTA e ENFERMEIRO veem todas as consultas, e PACIENTE vê apenas as \
                    próprias. Um login com perfil PACIENTE sem cadastro de paciente vinculado recebe uma \
                    lista vazia.

                    **Perfis com acesso:** MEDICO, RECEPCIONISTA, ENFERMEIRO e PACIENTE.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consultas retornadas com sucesso!"),
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
    ResponseEntity<PageResponse<AgendamentoDTO>> listarAgendamentos(
            @Parameter(hidden = true)
            @PageableDefault(size = 100, sort = "id") Pageable pageable,

            @Parameter(hidden = true)
            Authentication authentication);

    @Operation(
            summary = "Marca uma consulta",
            description = """
                    Marca uma consulta para o médico e o paciente informados. Antes de gravar, a agenda \
                    é conferida em duas etapas, nesta ordem:

                    1. O paciente não pode ter outra consulta no mesmo horário.
                    2. O médico precisa de um intervalo mínimo de 1 hora entre as consultas. Uma consulta \
                    exatamente 1 hora antes ou depois de outra é aceita.

                    Depois da gravação, a API publica o evento de consulta criada no RabbitMQ.

                    **Perfis com acesso:** MEDICO, RECEPCIONISTA e ENFERMEIRO.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Consulta marcada com sucesso!"),
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
                    description = "Médico ou paciente não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "O paciente já tem consulta no horário ou o médico não tem o intervalo mínimo de 1 hora!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    ResponseEntity<MensagemSucessoResponse> salvarAgendamento(@RequestBody @Valid SalvarAgendamentoRequest salvarAgendamentoRequest);

    @Operation(
            summary = "Remarca uma consulta",
            description = """
                    Altera o horário ou a observação da consulta. Campos omitidos ou nulos mantêm o valor \
                    atual. Médico e paciente não mudam por esta rota.

                    Quando o horário muda, valem as mesmas regras de agenda da marcação. A própria \
                    consulta fica fora da verificação, então confirmar o horário atual não gera conflito. \
                    Depois da gravação, a API publica o evento de consulta atualizada no RabbitMQ.

                    **Perfil com acesso:** RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta remarcada com sucesso!"),
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
                    description = "Consulta não encontrada!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "O paciente já tem consulta no horário ou o médico não tem o intervalo mínimo de 1 hora!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/{id}")
    ResponseEntity<MensagemSucessoResponse> atualizarAgendamento(
            @Parameter(description = "Id da consulta", example = "1")
            @PathVariable Integer id,

            @RequestBody @Valid AtualizarAgendamentoRequest atualizarAgendamentoRequest);

    @Operation(
            summary = "Cancela uma consulta",
            description = """
                    Cancela a consulta e remove o registro de forma definitiva, já que a consulta não tem \
                    situação de cadastro.

                    **Perfil com acesso:** RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Consulta cancelada com sucesso!"),
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
                    description = "Consulta não encontrada!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> cancelarAgendamento(
            @Parameter(description = "Id da consulta", example = "1")
            @PathVariable Integer id);
}