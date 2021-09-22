package es.melit.concesionario2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import es.melit.concesionario2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompradorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comprador.class);
        Comprador comprador1 = new Comprador();
        comprador1.setId(1L);
        Comprador comprador2 = new Comprador();
        comprador2.setId(comprador1.getId());
        assertThat(comprador1).isEqualTo(comprador2);
        comprador2.setId(2L);
        assertThat(comprador1).isNotEqualTo(comprador2);
        comprador1.setId(null);
        assertThat(comprador1).isNotEqualTo(comprador2);
    }
}
