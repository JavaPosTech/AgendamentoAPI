package br.com.fiap.agendamentoapi.controller.auth;

import br.com.fiap.agendamentoapi.model.request.auth.LoginRequest;
import br.com.fiap.agendamentoapi.model.response.auth.TokenResponse;
import br.com.fiap.agendamentoapi.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController implements AuthDocs {

    private final AuthService authService;

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest loginRequest) {
        return ResponseEntity.ok(new TokenResponse(authService.login(loginRequest)));
    }
}