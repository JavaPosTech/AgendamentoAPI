package br.com.fiap.agendamentoapi.model.entity.agendamento;

import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import br.com.fiap.agendamentoapi.model.entity.status.StatusConsulta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "agendamento", schema = "public")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_statusconsulta", nullable = false)
    private StatusConsulta statusConsulta;

    @Column(name = "datahora_consulta", nullable = false)
    private LocalDateTime dataHoraConsulta;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

}