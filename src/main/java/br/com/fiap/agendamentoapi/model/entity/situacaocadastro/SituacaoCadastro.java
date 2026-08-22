package br.com.fiap.agendamentoapi.model.entity.situacaocadastro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "situacao_cadastro", schema = "public")
public class SituacaoCadastro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String descricao;

}