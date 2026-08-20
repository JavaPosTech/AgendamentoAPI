package br.com.fiap.agendamentoapi.service.situacaocadastro;

import br.com.fiap.agendamentoapi.model.entity.situacaocadastro.SituacaoCadastro;
import br.com.fiap.agendamentoapi.repository.situacaocadastro.SituacaoCadastroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SituacaoCadastroService {

    private final SituacaoCadastroRepository situacaoCadastroRepository;

    @Transactional(readOnly = true)
    public SituacaoCadastro buscarReferenciaPorId(Integer id) {
        log.info("Buscando referência da Situação Cadastro com ID: [{}]", id);
        return situacaoCadastroRepository.getReferenceById(id);
    }
}