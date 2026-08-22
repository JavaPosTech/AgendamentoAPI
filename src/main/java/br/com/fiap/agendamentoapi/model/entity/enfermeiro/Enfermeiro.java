package br.com.fiap.agendamentoapi.model.entity.enfermeiro;

import br.com.fiap.agendamentoapi.model.entity.situacaocadastro.SituacaoCadastro;
import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "enfermeiro", schema = "public")
public class Enfermeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String sobrenome;

    @Column(nullable = false, unique = true, length = 20)
    private String coren;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_situacaocadastro", nullable = false)
    private SituacaoCadastro situacaoCadastro;

}