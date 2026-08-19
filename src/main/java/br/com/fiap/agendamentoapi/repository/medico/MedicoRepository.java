package br.com.fiap.agendamentoapi.repository.medico;

import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

}