package br.com.fiap.agendamentoapi.repository.paciente;

import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

}