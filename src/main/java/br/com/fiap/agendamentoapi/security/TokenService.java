package br.com.fiap.agendamentoapi.security;

import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class TokenService {

    private static final String ISSUER = "agendamento-api";

    @Value("${JWT_SECRET:agendamento-api-jwt-default-dev-secret-nao-utilizar-em-producao-2026}")
    private String secret;

    @Value("${JWT_EXPIRATION_MS:86400000}")
    private long expirationMs;

    public String gerarToken(Usuario usuario) {
        var agora = Instant.now();

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(usuario.getLogin())
                .claim("tipoUsuario", usuario.getTipoUsuario().getDescricao())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusMillis(expirationMs)))
                .signWith(getSigningKey())
                .compact();
    }

    public String validarToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .requireIssuer(ISSUER)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
