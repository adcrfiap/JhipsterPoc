package br.com.fiap.web.rest;

import br.com.fiap.domain.Intencao;
import br.com.fiap.repository.IntencaoRepository;
import br.com.fiap.service.IntencaoService;
import br.com.fiap.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.fiap.domain.Intencao}.
 */
@RestController
@RequestMapping("/api")
public class IntencaoResource {

    private final Logger log = LoggerFactory.getLogger(IntencaoResource.class);

    private static final String ENTITY_NAME = "intencao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntencaoService intencaoService;

    private final IntencaoRepository intencaoRepository;

    public IntencaoResource(IntencaoService intencaoService, IntencaoRepository intencaoRepository) {
        this.intencaoService = intencaoService;
        this.intencaoRepository = intencaoRepository;
    }

    /**
     * {@code POST  /intencaos} : Create a new intencao.
     *
     * @param intencao the intencao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new intencao, or with status {@code 400 (Bad Request)} if the intencao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/intencaos")
    public ResponseEntity<Intencao> createIntencao(@RequestBody Intencao intencao) throws URISyntaxException {
        log.debug("REST request to save Intencao : {}", intencao);
        if (intencao.getId() != null) {
            throw new BadRequestAlertException("A new intencao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Intencao result = intencaoService.save(intencao);
        return ResponseEntity
            .created(new URI("/api/intencaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /intencaos/:id} : Updates an existing intencao.
     *
     * @param id the id of the intencao to save.
     * @param intencao the intencao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intencao,
     * or with status {@code 400 (Bad Request)} if the intencao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the intencao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/intencaos/{id}")
    public ResponseEntity<Intencao> updateIntencao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Intencao intencao
    ) throws URISyntaxException {
        log.debug("REST request to update Intencao : {}, {}", id, intencao);
        if (intencao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intencao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!intencaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Intencao result = intencaoService.save(intencao);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intencao.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /intencaos/:id} : Partial updates given fields of an existing intencao, field will ignore if it is null
     *
     * @param id the id of the intencao to save.
     * @param intencao the intencao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intencao,
     * or with status {@code 400 (Bad Request)} if the intencao is not valid,
     * or with status {@code 404 (Not Found)} if the intencao is not found,
     * or with status {@code 500 (Internal Server Error)} if the intencao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/intencaos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Intencao> partialUpdateIntencao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Intencao intencao
    ) throws URISyntaxException {
        log.debug("REST request to partial update Intencao partially : {}, {}", id, intencao);
        if (intencao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intencao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!intencaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Intencao> result = intencaoService.partialUpdate(intencao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intencao.getId().toString())
        );
    }

    /**
     * {@code GET  /intencaos} : get all the intencaos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of intencaos in body.
     */
    @GetMapping("/intencaos")
    public ResponseEntity<List<Intencao>> getAllIntencaos(Pageable pageable) {
        log.debug("REST request to get a page of Intencaos");
        Page<Intencao> page = intencaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /intencaos/:id} : get the "id" intencao.
     *
     * @param id the id of the intencao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intencao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/intencaos/{id}")
    public ResponseEntity<Intencao> getIntencao(@PathVariable Long id) {
        log.debug("REST request to get Intencao : {}", id);
        Optional<Intencao> intencao = intencaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(intencao);
    }

    /**
     * {@code DELETE  /intencaos/:id} : delete the "id" intencao.
     *
     * @param id the id of the intencao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/intencaos/{id}")
    public ResponseEntity<Void> deleteIntencao(@PathVariable Long id) {
        log.debug("REST request to delete Intencao : {}", id);
        intencaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
