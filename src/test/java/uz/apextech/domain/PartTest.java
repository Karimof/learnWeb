package uz.apextech.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apextech.web.rest.TestUtil;

class PartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Part.class);
        Part part1 = new Part();
        part1.setId(1L);
        Part part2 = new Part();
        part2.setId(part1.getId());
        assertThat(part1).isEqualTo(part2);
        part2.setId(2L);
        assertThat(part1).isNotEqualTo(part2);
        part1.setId(null);
        assertThat(part1).isNotEqualTo(part2);
    }
}
