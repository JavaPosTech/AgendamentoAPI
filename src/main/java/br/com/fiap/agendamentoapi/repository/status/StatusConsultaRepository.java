package br.com.fiap.agendamentoapi.repository.status;

import br.com.fiap.agendamentoapi.model.entity.status.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusConsultaRepository extends JpaRepository<StatusConsulta, Integer> {

    Optional<StatusConsulta> findByDescricao(String descricao);

}