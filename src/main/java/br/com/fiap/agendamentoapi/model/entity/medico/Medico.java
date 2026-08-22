package br.com.fiap.agendamentoapi.model.entity.medico;

import br.com.fiap.agendamentoapi.model.entity.situacaocadastro.SituacaoCadastro;
import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "medico", schema = "public")
public class Medico {

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
    private String crm;

    @Column(nullable = false, length = 100)
    private String especialidade;

    @Column(nullable = false, length = 255)
    private String endereco;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_situacaocadastro", nullable = false)
    private SituacaoCadastro situacaoCadastro;

}