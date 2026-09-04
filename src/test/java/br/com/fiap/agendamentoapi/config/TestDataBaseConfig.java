package br.com.fiap.agendamentoapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class TestDataBaseConfig {

    @Value("${DATABASE_IP:localhost}")
    private String ip;

    @Value("${DATABASE_USER:postgres}")
    private String usuario;

    @Value("${DATABASE_PASSWORD:Ggu171905**}")
    private String senha;

    @Value("${DATABASE_PORT:8745}")
    private String porta;

    @Value("${DATABASE_NAME:postgres}")
    private String nome;

    private String getUrl() {
        return String.format("jdbc:postgresql://%s:%s/%s", ip, porta, nome);
    }

    @Bean
    public DataSource dataSourceTest() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(getUrl());
        dataSourceBuilder.username(usuario);
        dataSourceBuilder.password(senha);

        return dataSourceBuilder.build();
    }
}