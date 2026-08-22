package br.com.fiap.agendamentoapi.repository.enfermeiro;

import br.com.fiap.agendamentoapi.model.entity.enfermeiro.Enfermeiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnfermeiroRepository extends JpaRepository<Enfermeiro, Integer> {

    @EntityGraph(attributePaths = {"usuario", "situacaoCadastro"})
    Page<Enfermeiro> findAll(Pageable pageable);

}