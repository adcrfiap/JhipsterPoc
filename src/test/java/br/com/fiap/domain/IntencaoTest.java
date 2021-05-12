package br.com.fiap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntencaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intencao.class);
        Intencao intencao1 = new Intencao();
        intencao1.setId(1L);
        Intencao intencao2 = new Intencao();
        intencao2.setId(intencao1.getId());
        assertThat(intencao1).isEqualTo(intencao2);
        intencao2.setId(2L);
        assertThat(intencao1).isNotEqualTo(intencao2);
        intencao1.setId(null);
        assertThat(intencao1).isNotEqualTo(intencao2);
    }
}
