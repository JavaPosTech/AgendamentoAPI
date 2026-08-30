package br.com.fiap.agendamentoapi.model.entity.historicopaciente;

import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "historico_paciente", schema = "public")
public class HistoricoPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @Column(name = "queixa_principal", length = 500)
    private String queixaPrincipal;

    @Column(name = "historico_doenca", nullable = false, columnDefinition = "TEXT")
    private String historicoDoenca;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String medicamentos;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String alergias;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

}
