package bc.gov.banbeis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import bc.gov.banbeis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubNavigationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubNavigation.class);
    }
}
