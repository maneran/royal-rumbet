package com.sheepit.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheepit.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameConfig.class);
        GameConfig gameConfig1 = new GameConfig();
        gameConfig1.setId(1L);
        GameConfig gameConfig2 = new GameConfig();
        gameConfig2.setId(gameConfig1.getId());
        assertThat(gameConfig1).isEqualTo(gameConfig2);
        gameConfig2.setId(2L);
        assertThat(gameConfig1).isNotEqualTo(gameConfig2);
        gameConfig1.setId(null);
        assertThat(gameConfig1).isNotEqualTo(gameConfig2);
    }
}
