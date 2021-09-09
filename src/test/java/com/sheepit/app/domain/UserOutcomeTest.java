package com.sheepit.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheepit.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserOutcomeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserOutcome.class);
        UserOutcome userOutcome1 = new UserOutcome();
        userOutcome1.setId(1L);
        UserOutcome userOutcome2 = new UserOutcome();
        userOutcome2.setId(userOutcome1.getId());
        assertThat(userOutcome1).isEqualTo(userOutcome2);
        userOutcome2.setId(2L);
        assertThat(userOutcome1).isNotEqualTo(userOutcome2);
        userOutcome1.setId(null);
        assertThat(userOutcome1).isNotEqualTo(userOutcome2);
    }
}
