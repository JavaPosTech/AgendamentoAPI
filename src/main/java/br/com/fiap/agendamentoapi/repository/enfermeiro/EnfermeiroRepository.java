package br.com.fiap.agendamentoapi.repository.enfermeiro;

import br.com.fiap.agendamentoapi.model.entity.enfermeiro.Enfermeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnfermeiroRepository extends JpaRepository<Enfermeiro, Integer> {

}