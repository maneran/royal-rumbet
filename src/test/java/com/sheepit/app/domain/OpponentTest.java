package com.sheepit.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheepit.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpponentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opponent.class);
        Opponent opponent1 = new Opponent();
        opponent1.setId(1L);
        Opponent opponent2 = new Opponent();
        opponent2.setId(opponent1.getId());
        assertThat(opponent1).isEqualTo(opponent2);
        opponent2.setId(2L);
        assertThat(opponent1).isNotEqualTo(opponent2);
        opponent1.setId(null);
        assertThat(opponent1).isNotEqualTo(opponent2);
    }
}
