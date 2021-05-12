package br.com.fiap.repository;

import br.com.fiap.domain.Intencao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Intencao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntencaoRepository extends JpaRepository<Intencao, Long> {}
