package br.com.fiap.agendamentoapi.repository.situacaocadastro;

import br.com.fiap.agendamentoapi.model.entity.situacaocadastro.SituacaoCadastro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SituacaoCadastroRepository extends JpaRepository<SituacaoCadastro, Integer> {

}