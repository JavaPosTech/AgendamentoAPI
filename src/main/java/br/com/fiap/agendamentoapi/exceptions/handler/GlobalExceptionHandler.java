package br.com.fiap.agendamentoapi.exceptions.handler;

import br.com.fiap.agendamentoapi.exceptions.*;
import br.com.fiap.agendamentoapi.exceptions.dto.ErrorResponseDTO;
import br.com.fiap.agendamentoapi.exceptions.dto.MethodArgumentNotValidResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest pHttpServletRequest) {

        var errors = ex.getFieldErrors()
                .stream()
                .map(fieldError -> new MethodArgumentNotValidResponseDTO(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();

        var response = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/validation-error",
                "A requisição contém dados inválidos!",
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Inválida!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/unreadable-message",
                ex.getMostSpecificCause().getMessage(),
                ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(SenhaIncorretaException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPasswordException(SenhaIncorretaException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Senha Incorreta!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/invalid-password",
                ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Inválida!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/illegal-argument",
                "A requisição contém dados inválidos.",
                ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UsuarioNaoEncontradoException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Usuário não encontrado!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/usuario-not-found",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(TipoUsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleTipoUsuarioNaoEncontradoException(TipoUsuarioNaoEncontradoException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Tipo Usuário não encontrado!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/tipo-usuario-not-found",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConsultaNaoEncontradaException.class)
    public ResponseEntity<ErrorResponseDTO> handleConsultaNaoEncontradaException(ConsultaNaoEncontradaException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Consulta não encontrada!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/consulta-not-found",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MedicoIndisponivelException.class)
    public ResponseEntity<ErrorResponseDTO> handleMedicoIndisponivelException(MedicoIndisponivelException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Horário Indisponível!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/medico-indisponivel",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Registro não encontrado!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/entity-not-found",
                "Não foi possível localizar um registro com o ID informado!",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/resource-not-found",
                "O endpoint informado não existe!",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResourceException(DataIntegrityViolationException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflito de Dados!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/data-integrity-violation",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleInternalServerErrorException(Exception ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno no Servidor!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/internal-server-error",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, @NonNull AuthenticationException authException) throws IOException {
        var errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Não Autorizado!",
                request.getRequestURI(),
                "/AgendamentoAPI/problems/unauthorized",
                "Token de acesso ausente, inválido ou expirado!"
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, @NonNull AccessDeniedException accessDeniedException) throws IOException {
        var errorResponse = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado!",
                request.getRequestURI(),
                "/AgendamentoAPI/problems/access-denied",
                "Você não tem permissão para acessar este recurso!"
        );

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    @ExceptionHandler(HistoricoPacienteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleHistoricoPacienteNaoEncontradoException(HistoricoPacienteNaoEncontradoException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Histórico do paciente não encontrado!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/historico-paciente-not-found",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UsuarioInativoException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsuarioInativoException(UsuarioInativoException ex, HttpServletRequest pHttpServletRequest) {

        var response = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Usuário Inativo!",
                pHttpServletRequest.getRequestURI(),
                "/AgendamentoAPI/problems/user-inactive",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
