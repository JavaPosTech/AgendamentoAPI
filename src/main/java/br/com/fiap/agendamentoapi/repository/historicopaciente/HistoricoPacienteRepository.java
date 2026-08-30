package br.com.fiap.agendamentoapi.repository.historicopaciente;

import br.com.fiap.agendamentoapi.model.entity.historicopaciente.HistoricoPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoPacienteRepository extends JpaRepository<HistoricoPaciente, Integer> {

    @EntityGraph(attributePaths = {"paciente"})
    Page<HistoricoPaciente> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"paciente"})
    Page<HistoricoPaciente> findByPacienteId(Integer pacienteId, Pageable pageable);

}
