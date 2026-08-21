package br.com.fiap.agendamentoapi.repository.medico;

import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    @EntityGraph(attributePaths = {"usuario", "situacaoCadastro"})
    Page<Medico> findAll(Pageable pageable);

}