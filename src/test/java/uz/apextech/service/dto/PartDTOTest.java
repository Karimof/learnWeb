package uz.apextech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apextech.web.rest.TestUtil;

class PartDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartDTO.class);
        PartDTO partDTO1 = new PartDTO();
        partDTO1.setId(1L);
        PartDTO partDTO2 = new PartDTO();
        assertThat(partDTO1).isNotEqualTo(partDTO2);
        partDTO2.setId(partDTO1.getId());
        assertThat(partDTO1).isEqualTo(partDTO2);
        partDTO2.setId(2L);
        assertThat(partDTO1).isNotEqualTo(partDTO2);
        partDTO1.setId(null);
        assertThat(partDTO1).isNotEqualTo(partDTO2);
    }
}
