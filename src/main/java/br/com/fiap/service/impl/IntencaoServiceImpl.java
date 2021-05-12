package br.com.fiap.service.impl;

import br.com.fiap.domain.Intencao;
import br.com.fiap.repository.IntencaoRepository;
import br.com.fiap.service.IntencaoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Intencao}.
 */
@Service
@Transactional
public class IntencaoServiceImpl implements IntencaoService {

    private final Logger log = LoggerFactory.getLogger(IntencaoServiceImpl.class);

    private final IntencaoRepository intencaoRepository;

    public IntencaoServiceImpl(IntencaoRepository intencaoRepository) {
        this.intencaoRepository = intencaoRepository;
    }

    @Override
    public Intencao save(Intencao intencao) {
        log.debug("Request to save Intencao : {}", intencao);
        return intencaoRepository.save(intencao);
    }

    @Override
    public Optional<Intencao> partialUpdate(Intencao intencao) {
        log.debug("Request to partially update Intencao : {}", intencao);

        return intencaoRepository
            .findById(intencao.getId())
            .map(
                existingIntencao -> {
                    if (intencao.getDescricao() != null) {
                        existingIntencao.setDescricao(intencao.getDescricao());
                    }
                    if (intencao.getValorEstimado() != null) {
                        existingIntencao.setValorEstimado(intencao.getValorEstimado());
                    }
                    if (intencao.getData() != null) {
                        existingIntencao.setData(intencao.getData());
                    }

                    return existingIntencao;
                }
            )
            .map(intencaoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Intencao> findAll(Pageable pageable) {
        log.debug("Request to get all Intencaos");
        return intencaoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Intencao> findOne(Long id) {
        log.debug("Request to get Intencao : {}", id);
        return intencaoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Intencao : {}", id);
        intencaoRepository.deleteById(id);
    }
}
