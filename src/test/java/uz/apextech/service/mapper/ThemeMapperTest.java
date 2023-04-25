package uz.apextech.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThemeMapperTest {

    private ThemeMapper themeMapper;

    @BeforeEach
    public void setUp() {
        themeMapper = new ThemeMapperImpl();
    }
}
