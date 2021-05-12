package br.com.fiap.service;

import br.com.fiap.domain.Produto;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Produto}.
 */
public interface ProdutoService {
    /**
     * Save a produto.
     *
     * @param produto the entity to save.
     * @return the persisted entity.
     */
    Produto save(Produto produto);

    /**
     * Partially updates a produto.
     *
     * @param produto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Produto> partialUpdate(Produto produto);

    /**
     * Get all the produtos.
     *
     * @return the list of entities.
     */
    List<Produto> findAll();

    /**
     * Get the "id" produto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Produto> findOne(Long id);

    /**
     * Delete the "id" produto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
