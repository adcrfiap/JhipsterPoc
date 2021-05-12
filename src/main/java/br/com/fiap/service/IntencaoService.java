package br.com.fiap.service;

import br.com.fiap.domain.Intencao;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Intencao}.
 */
public interface IntencaoService {
    /**
     * Save a intencao.
     *
     * @param intencao the entity to save.
     * @return the persisted entity.
     */
    Intencao save(Intencao intencao);

    /**
     * Partially updates a intencao.
     *
     * @param intencao the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Intencao> partialUpdate(Intencao intencao);

    /**
     * Get all the intencaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Intencao> findAll(Pageable pageable);

    /**
     * Get the "id" intencao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Intencao> findOne(Long id);

    /**
     * Delete the "id" intencao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
