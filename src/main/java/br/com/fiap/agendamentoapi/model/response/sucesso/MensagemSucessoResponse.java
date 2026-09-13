package br.com.fiap.agendamentoapi.model.response.sucesso;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Modelo de resposta padrão indicando que a operação foi concluída com sucesso.")
public record MensagemSucessoResponse(

        @Schema(description = "Código HTTP da resposta.", example = "201")
        int status,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora da resposta.", example = "12/09/2026 - 14:30:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime timestamp,

        @Schema(description = "Mensagem de sucesso.", example = "Paciente criado com sucesso!")
        String mensagem

) {
    public MensagemSucessoResponse(int pStatus, String pMessage) {
        this(pStatus, LocalDateTime.now(), pMessage);
    }
}