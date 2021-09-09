package com.sheepit.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sheepit.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminOutcomeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminOutcome.class);
        AdminOutcome adminOutcome1 = new AdminOutcome();
        adminOutcome1.setId(1L);
        AdminOutcome adminOutcome2 = new AdminOutcome();
        adminOutcome2.setId(adminOutcome1.getId());
        assertThat(adminOutcome1).isEqualTo(adminOutcome2);
        adminOutcome2.setId(2L);
        assertThat(adminOutcome1).isNotEqualTo(adminOutcome2);
        adminOutcome1.setId(null);
        assertThat(adminOutcome1).isNotEqualTo(adminOutcome2);
    }
}
