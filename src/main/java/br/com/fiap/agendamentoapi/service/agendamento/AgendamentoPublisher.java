package br.com.fiap.agendamentoapi.service.agendamento;

import br.com.fiap.agendamentoapi.config.RabbitMQConfig;
import br.com.fiap.agendamentoapi.model.event.agendamento.AgendamentoAtualizadoEvent;
import br.com.fiap.agendamentoapi.model.event.agendamento.AgendamentoCriadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgendamentoPublisher {

    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publicarAgendamentoCriado(AgendamentoCriadoEvent evento) {
        try {
            log.info(
                    "Publicando evento de consulta criada - Agendamento: [ID: {}] - Event ID: [{}]",
                    evento.agendamentoId(),
                    evento.eventId()
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.AGENDAMENTO_EXCHANGE,
                    RabbitMQConfig.AGENDAMENTO_CRIADO_ROUTING_KEY,
                    evento
            );

            log.info(
                    "Evento de consulta criada publicado com sucesso - Agendamento: [ID: {}]",
                    evento.agendamentoId()
            );
        } catch (AmqpException exception) {
            log.error(
                    "Erro ao publicar evento de consulta criada - Agendamento: [ID: {}]",
                    evento.agendamentoId(),
                    exception
            );
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publicarAgendamentoAtualizado(AgendamentoAtualizadoEvent evento) {
        try {
            log.info(
                    "Publicando evento de consulta atualizada - Agendamento: [ID: {}] - Event ID: [{}]",
                    evento.agendamentoId(),
                    evento.eventId()
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.AGENDAMENTO_EXCHANGE,
                    RabbitMQConfig.AGENDAMENTO_ATUALIZADO_ROUTING_KEY,
                    evento
            );

            log.info(
                    "Evento de consulta atualizada publicado com sucesso - Agendamento: [ID: {}]",
                    evento.agendamentoId()
            );
        } catch (AmqpException exception) {
            log.error(
                    "Erro ao publicar evento de consulta atualizada - Agendamento: [ID: {}]",
                    evento.agendamentoId(),
                    exception
            );
        }
    }
}
