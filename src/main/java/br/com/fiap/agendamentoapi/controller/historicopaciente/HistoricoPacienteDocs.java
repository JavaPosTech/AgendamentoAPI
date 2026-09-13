package br.com.fiap.agendamentoapi.controller.historicopaciente;

import br.com.fiap.agendamentoapi.controller.ParametrosPaginacao;
import br.com.fiap.agendamentoapi.exceptions.dto.ErrorResponseDTO;
import br.com.fiap.agendamentoapi.model.dto.historicopaciente.HistoricoPacienteDTO;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.AtualizarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.SalvarHistoricoPacienteRequest;
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

@Tag(name = "Histórico do Paciente", description = "Endpoints relacionados ao gerenciamento do Histórico dos Pacientes")
public interface HistoricoPacienteDocs {

    @ParametrosPaginacao
    @Operation(
            summary = "Lista os históricos dos pacientes",
            description = """
                    Retorna os históricos clínicos de todos os pacientes de forma paginada. Cada registro \
                    traz o id e o nome completo do paciente a que pertence.

                    **Perfis com acesso:** MEDICO, RECEPCIONISTA e ENFERMEIRO.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Históricos retornados com sucesso!"),
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
    ResponseEntity<PageResponse<HistoricoPacienteDTO>> listarHistoricosPaciente(
            @Parameter(hidden = true)
            @PageableDefault(size = 100, sort = "id") Pageable pageable);

    @Operation(
            summary = "Cadastra um histórico de paciente",
            description = """
                    Registra um histórico clínico para o paciente informado. Um mesmo paciente pode ter \
                    vários históricos.

                    **Perfis com acesso:** MEDICO e RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Histórico do paciente cadastrado com sucesso!"),
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
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    ResponseEntity<MensagemSucessoResponse> salvarHistoricoPaciente(@RequestBody @Valid SalvarHistoricoPacienteRequest salvarHistoricoPacienteRequest);

    @Operation(
            summary = "Atualiza um histórico de paciente",
            description = """
                    Atualiza parcialmente o histórico clínico. Campos omitidos, nulos, vazios ou só com \
                    espaços mantêm o valor atual. O paciente vinculado ao histórico não muda por esta rota.

                    **Perfis com acesso:** MEDICO e RECEPCIONISTA.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Histórico do paciente atualizado com sucesso!"),
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
                    description = "Histórico do paciente não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/{id}")
    ResponseEntity<MensagemSucessoResponse> atualizarHistoricoPaciente(
            @Parameter(description = "Id do histórico do paciente", example = "1")
            @PathVariable Integer id,

            @RequestBody @Valid AtualizarHistoricoPacienteRequest atualizarHistoricoPacienteRequest);

    @Operation(
            summary = "Exclui um histórico de paciente",
            description = """
                    Remove o histórico clínico de forma definitiva. Diferente dos cadastros, o histórico \
                    não tem situação de cadastro, então a exclusão apaga o registro do banco.

                    **Perfil com acesso:** ADMINISTRADOR.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Histórico do paciente excluído com sucesso!"),
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
                    description = "Histórico do paciente não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarHistoricoPaciente(
            @Parameter(description = "Id do histórico do paciente", example = "1")
            @PathVariable Integer id);
}