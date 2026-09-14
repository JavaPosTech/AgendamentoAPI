package br.com.fiap.agendamentoapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AgendamentoAPI")
                        .version("1.0.0")
                        .description("""
                                API responsável pelo agendamento de consultas médicas e pelo cadastro de \
                                médicos, enfermeiros, recepcionistas e pacientes, com o histórico clínico \
                                de cada paciente.

                                ### Autenticação

                                1. Chame **POST /v1/auth/login** com login e senha.
                                2. Copie o valor do campo token da resposta.
                                3. Clique em **Authorize** e cole o token.

                                A partir daí todas as chamadas desta página seguem autenticadas. O token \
                                expira em 2 horas por padrão.

                                ### Perfis de acesso

                                Cada rota informa, na própria descrição, os perfis que podem chamá-la. \
                                Os perfis são ADMINISTRADOR, MEDICO, ENFERMEIRO, RECEPCIONISTA e PACIENTE. \
                                Sem token a API responde **401**, e com um perfil sem permissão responde **403**.

                                ### Convenções

                                * **Datas:** dd/MM/yyyy para datas e dd/MM/yyyy - HH:mm:ss para data e hora, \
                                tanto no envio quanto na resposta.
                                * **Paginação:** as listagens começam na página **1** e trazem 100 registros \
                                por página, ordenados por id, quando nada é informado.
                                * **Atualização parcial:** as rotas PATCH alteram só os campos enviados \
                                preenchidos. Os demais mantêm o valor atual.
                                * **JSON estrito:** um campo que não existe no modelo faz a requisição \
                                ser recusada com **400**.
                                * **Erros:** toda falha segue o modelo ErrorResponseDTO.
                                """))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT devolvido por POST /v1/auth/login. Informe apenas o token, sem o prefixo Bearer.")));
    }
}