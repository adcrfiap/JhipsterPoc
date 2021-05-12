package br.com.fiap.service.impl;

import br.com.fiap.domain.Produto;
import br.com.fiap.repository.ProdutoRepository;
import br.com.fiap.service.ProdutoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Produto}.
 */
@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    private final Logger log = LoggerFactory.getLogger(ProdutoServiceImpl.class);

    private final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto save(Produto produto) {
        log.debug("Request to save Produto : {}", produto);
        return produtoRepository.save(produto);
    }

    @Override
    public Optional<Produto> partialUpdate(Produto produto) {
        log.debug("Request to partially update Produto : {}", produto);

        return produtoRepository
            .findById(produto.getId())
            .map(
                existingProduto -> {
                    if (produto.getNome() != null) {
                        existingProduto.setNome(produto.getNome());
                    }
                    if (produto.getModelo() != null) {
                        existingProduto.setModelo(produto.getModelo());
                    }
                    if (produto.getCategoria() != null) {
                        existingProduto.setCategoria(produto.getCategoria());
                    }
                    if (produto.getMarca() != null) {
                        existingProduto.setMarca(produto.getMarca());
                    }

                    return existingProduto;
                }
            )
            .map(produtoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> findAll() {
        log.debug("Request to get all Produtos");
        return produtoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produto> findOne(Long id) {
        log.debug("Request to get Produto : {}", id);
        return produtoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Produto : {}", id);
        produtoRepository.deleteById(id);
    }
}
