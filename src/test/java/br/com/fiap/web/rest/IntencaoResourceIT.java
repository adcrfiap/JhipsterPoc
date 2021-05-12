package br.com.fiap.web.rest;

import static br.com.fiap.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.fiap.IntegrationTest;
import br.com.fiap.domain.Intencao;
import br.com.fiap.repository.IntencaoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IntencaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IntencaoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_ESTIMADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ESTIMADO = new BigDecimal(2);

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/intencaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntencaoRepository intencaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIntencaoMockMvc;

    private Intencao intencao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intencao createEntity(EntityManager em) {
        Intencao intencao = new Intencao().descricao(DEFAULT_DESCRICAO).valorEstimado(DEFAULT_VALOR_ESTIMADO).data(DEFAULT_DATA);
        return intencao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intencao createUpdatedEntity(EntityManager em) {
        Intencao intencao = new Intencao().descricao(UPDATED_DESCRICAO).valorEstimado(UPDATED_VALOR_ESTIMADO).data(UPDATED_DATA);
        return intencao;
    }

    @BeforeEach
    public void initTest() {
        intencao = createEntity(em);
    }

    @Test
    @Transactional
    void createIntencao() throws Exception {
        int databaseSizeBeforeCreate = intencaoRepository.findAll().size();
        // Create the Intencao
        restIntencaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intencao)))
            .andExpect(status().isCreated());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeCreate + 1);
        Intencao testIntencao = intencaoList.get(intencaoList.size() - 1);
        assertThat(testIntencao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testIntencao.getValorEstimado()).isEqualByComparingTo(DEFAULT_VALOR_ESTIMADO);
        assertThat(testIntencao.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    void createIntencaoWithExistingId() throws Exception {
        // Create the Intencao with an existing ID
        intencao.setId(1L);

        int databaseSizeBeforeCreate = intencaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntencaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intencao)))
            .andExpect(status().isBadRequest());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIntencaos() throws Exception {
        // Initialize the database
        intencaoRepository.saveAndFlush(intencao);

        // Get all the intencaoList
        restIntencaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intencao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valorEstimado").value(hasItem(sameNumber(DEFAULT_VALOR_ESTIMADO))))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @Test
    @Transactional
    void getIntencao() throws Exception {
        // Initialize the database
        intencaoRepository.saveAndFlush(intencao);

        // Get the intencao
        restIntencaoMockMvc
            .perform(get(ENTITY_API_URL_ID, intencao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intencao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.valorEstimado").value(sameNumber(DEFAULT_VALOR_ESTIMADO)))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIntencao() throws Exception {
        // Get the intencao
        restIntencaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIntencao() throws Exception {
        // Initialize the database
        intencaoRepository.saveAndFlush(intencao);

        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();

        // Update the intencao
        Intencao updatedIntencao = intencaoRepository.findById(intencao.getId()).get();
        // Disconnect from session so that the updates on updatedIntencao are not directly saved in db
        em.detach(updatedIntencao);
        updatedIntencao.descricao(UPDATED_DESCRICAO).valorEstimado(UPDATED_VALOR_ESTIMADO).data(UPDATED_DATA);

        restIntencaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIntencao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIntencao))
            )
            .andExpect(status().isOk());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
        Intencao testIntencao = intencaoList.get(intencaoList.size() - 1);
        assertThat(testIntencao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testIntencao.getValorEstimado()).isEqualTo(UPDATED_VALOR_ESTIMADO);
        assertThat(testIntencao.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void putNonExistingIntencao() throws Exception {
        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();
        intencao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntencaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intencao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intencao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntencao() throws Exception {
        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();
        intencao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntencaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intencao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntencao() throws Exception {
        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();
        intencao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntencaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intencao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntencaoWithPatch() throws Exception {
        // Initialize the database
        intencaoRepository.saveAndFlush(intencao);

        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();

        // Update the intencao using partial update
        Intencao partialUpdatedIntencao = new Intencao();
        partialUpdatedIntencao.setId(intencao.getId());

        partialUpdatedIntencao.descricao(UPDATED_DESCRICAO);

        restIntencaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntencao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntencao))
            )
            .andExpect(status().isOk());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
        Intencao testIntencao = intencaoList.get(intencaoList.size() - 1);
        assertThat(testIntencao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testIntencao.getValorEstimado()).isEqualByComparingTo(DEFAULT_VALOR_ESTIMADO);
        assertThat(testIntencao.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    void fullUpdateIntencaoWithPatch() throws Exception {
        // Initialize the database
        intencaoRepository.saveAndFlush(intencao);

        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();

        // Update the intencao using partial update
        Intencao partialUpdatedIntencao = new Intencao();
        partialUpdatedIntencao.setId(intencao.getId());

        partialUpdatedIntencao.descricao(UPDATED_DESCRICAO).valorEstimado(UPDATED_VALOR_ESTIMADO).data(UPDATED_DATA);

        restIntencaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntencao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntencao))
            )
            .andExpect(status().isOk());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
        Intencao testIntencao = intencaoList.get(intencaoList.size() - 1);
        assertThat(testIntencao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testIntencao.getValorEstimado()).isEqualByComparingTo(UPDATED_VALOR_ESTIMADO);
        assertThat(testIntencao.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void patchNonExistingIntencao() throws Exception {
        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();
        intencao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntencaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intencao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intencao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntencao() throws Exception {
        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();
        intencao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntencaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intencao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntencao() throws Exception {
        int databaseSizeBeforeUpdate = intencaoRepository.findAll().size();
        intencao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntencaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(intencao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intencao in the database
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntencao() throws Exception {
        // Initialize the database
        intencaoRepository.saveAndFlush(intencao);

        int databaseSizeBeforeDelete = intencaoRepository.findAll().size();

        // Delete the intencao
        restIntencaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, intencao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intencao> intencaoList = intencaoRepository.findAll();
        assertThat(intencaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
