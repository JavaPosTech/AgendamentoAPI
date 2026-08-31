package br.com.fiap.agendamentoapi.repository.recepcionista;

import br.com.fiap.agendamentoapi.model.entity.recepcionista.Recepcionista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Integer> {

    @EntityGraph(attributePaths = {"usuario", "situacaoCadastro"})
    Page<Recepcionista> findAll(Pageable pageable);
}
