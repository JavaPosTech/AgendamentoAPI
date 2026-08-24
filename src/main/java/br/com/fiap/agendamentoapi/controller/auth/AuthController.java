package br.com.fiap.agendamentoapi.controller.auth;

import br.com.fiap.agendamentoapi.model.request.auth.LoginRequest;
import br.com.fiap.agendamentoapi.model.response.auth.TokenResponse;
import br.com.fiap.agendamentoapi.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
//para logar com o usuario e pegar o token gerado para poder utilizar nos endpoints necessarios get/post/path
@Tag(name = "Autenticação", description = "Endpoints relacionados à autenticação de Usuários")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(new TokenResponse(authService.login(loginRequest)));
    }
}
