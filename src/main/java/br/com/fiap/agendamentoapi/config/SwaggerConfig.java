package br.com.fiap.agendamentoapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Agendamento API")
                        .version("1.0.0")
                        .description("""
                                API responsável pelo gerenciamento de consultas médicas,
                                permitindo o agendamento, edição, consulta e cancelamento
                                de consultas, além de autenticação e autorização de usuários.
                                """));
    }
}