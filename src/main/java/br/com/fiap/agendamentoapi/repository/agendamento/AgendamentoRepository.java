package br.com.fiap.agendamentoapi.repository.agendamento;

import br.com.fiap.agendamentoapi.model.entity.agendamento.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    boolean existsByMedicoIdAndDataHoraConsulta(Integer medicoId, LocalDateTime dataHoraConsulta);

    boolean existsByPacienteIdAndDataHoraConsulta(Integer pacienteId, LocalDateTime dataHoraConsulta);

    Page<Agendamento> findByPacienteId(Integer pacienteId, Pageable pageable);

}