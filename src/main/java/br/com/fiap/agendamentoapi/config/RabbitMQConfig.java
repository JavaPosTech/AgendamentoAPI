package br.com.fiap.agendamentoapi.config;

import br.com.fiap.agendamentoapi.model.rabbitmq.AgendamentoCriadoEvent;
import br.com.fiap.agendamentoapi.model.rabbitmq.AgendamentoAtualizadoEvent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String AGENDAMENTO_EXCHANGE = "agendamento.events";
    public static final String AGENDAMENTO_CRIADO_TYPE_ID = "agendamento.criado.v1";
    public static final String AGENDAMENTO_CRIADO_ROUTING_KEY = "agendamento.criado";
    public static final String AGENDAMENTO_ATUALIZADO_TYPE_ID = "agendamento.atualizado.v1";
    public static final String AGENDAMENTO_ATUALIZADO_ROUTING_KEY = "agendamento.atualizado";
    public static final String NOTIFICACAO_AGENDAMENTO_QUEUE =  "notificacao.email.agendamento";

    @Bean
    public DirectExchange agendamentoExchange() {
        return new DirectExchange(AGENDAMENTO_EXCHANGE, true, false);
    }

    @Bean
    public Queue notificacaoAgendamentoQueue() {
        return QueueBuilder
                .durable(NOTIFICACAO_AGENDAMENTO_QUEUE)
                .build();
    }

    @Bean
    public Binding agendamentoCriadoBinding(Queue notificacaoAgendamentoQueue, DirectExchange agendamentoExchange) {
        return BindingBuilder
                .bind(notificacaoAgendamentoQueue)
                .to(agendamentoExchange)
                .with(AGENDAMENTO_CRIADO_ROUTING_KEY);
    }

    @Bean
    public Binding agendamentoAtualizadoBinding(Queue notificacaoAgendamentoQueue, DirectExchange agendamentoExchange) {
        return BindingBuilder
                .bind(notificacaoAgendamentoQueue)
                .to(agendamentoExchange)
                .with(AGENDAMENTO_ATUALIZADO_ROUTING_KEY);
    }

    @Bean
    public DefaultClassMapper rabbitClassMapper() {
        var classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(
                Map.of(
                        AGENDAMENTO_CRIADO_TYPE_ID,
                        AgendamentoCriadoEvent.class,

                        AGENDAMENTO_ATUALIZADO_TYPE_ID,
                        AgendamentoAtualizadoEvent.class
                )
        );

        return classMapper;
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter(DefaultClassMapper rabbitClassMapper) {
        var jsonMessageConverter = new JacksonJsonMessageConverter();
        jsonMessageConverter.setClassMapper(rabbitClassMapper);

        return jsonMessageConverter;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JacksonJsonMessageConverter jsonMessageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);

        return rabbitTemplate;
    }
}