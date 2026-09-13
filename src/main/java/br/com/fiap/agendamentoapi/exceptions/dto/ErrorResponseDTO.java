package br.com.fiap.agendamentoapi.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.net.URI;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Estrutura genérica para respostas de erro da API")
public record ErrorResponseDTO(

        @Schema(description = "Código HTTP.", example = "404")
        int status,

        @Schema(description = "Título resumido do erro.", example = "Usuário não encontrado!")
        String title,

        @Schema(description = "Endpoint da requisição.", example = "/AgendamentoAPI/v1/medico/99")
        String instance,

        @Schema(description = "URI identificadora do tipo de erro.", example = "/AgendamentoAPI/problems/usuario-not-found")
        URI type,

        @Schema(description = "Mensagem detalhada.", example = "Médico não encontrado!")
        String detail,

        @Schema(description = "Detalhes adicionais, presentes só em alguns erros. Na validação de campos, traz a lista de MethodArgumentNotValidResponseDTO.")
        Object errors,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora do erro.", example = "12/09/2026 - 14:30:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime timestamp

) {
    public ErrorResponseDTO(int pStatus, String pTitle, String pInstance, String pType, String pDetail, Object pErrors) {
        this(pStatus, pTitle, pInstance, URI.create(pType), pDetail, pErrors, LocalDateTime.now());
    }

    public ErrorResponseDTO(int pStatus, String pTitle, String pInstance, String pType, String pDetail) {
        this(pStatus, pTitle, pInstance, URI.create(pType), pDetail, null, LocalDateTime.now());
    }
}