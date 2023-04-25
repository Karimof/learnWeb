package uz.apextech.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.apextech.IntegrationTest;
import uz.apextech.domain.Course;
import uz.apextech.domain.Theme;
import uz.apextech.repository.ThemeRepository;
import uz.apextech.service.criteria.ThemeCriteria;
import uz.apextech.service.dto.ThemeDTO;
import uz.apextech.service.mapper.ThemeMapper;

/**
 * Integration tests for the {@link ThemeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ThemeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/themes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeMapper themeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThemeMockMvc;

    private Theme theme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theme createEntity(EntityManager em) {
        Theme theme = new Theme().title(DEFAULT_TITLE);
        return theme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theme createUpdatedEntity(EntityManager em) {
        Theme theme = new Theme().title(UPDATED_TITLE);
        return theme;
    }

    @BeforeEach
    public void initTest() {
        theme = createEntity(em);
    }

    @Test
    @Transactional
    void createTheme() throws Exception {
        int databaseSizeBeforeCreate = themeRepository.findAll().size();
        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);
        restThemeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isCreated());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeCreate + 1);
        Theme testTheme = themeList.get(themeList.size() - 1);
        assertThat(testTheme.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createThemeWithExistingId() throws Exception {
        // Create the Theme with an existing ID
        theme.setId(1L);
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        int databaseSizeBeforeCreate = themeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restThemeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeRepository.findAll().size();
        // set the field null
        theme.setTitle(null);

        // Create the Theme, which fails.
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        restThemeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllThemes() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList
        restThemeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theme.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get the theme
        restThemeMockMvc
            .perform(get(ENTITY_API_URL_ID, theme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(theme.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getThemesByIdFiltering() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        Long id = theme.getId();

        defaultThemeShouldBeFound("id.equals=" + id);
        defaultThemeShouldNotBeFound("id.notEquals=" + id);

        defaultThemeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultThemeShouldNotBeFound("id.greaterThan=" + id);

        defaultThemeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultThemeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllThemesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where title equals to DEFAULT_TITLE
        defaultThemeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the themeList where title equals to UPDATED_TITLE
        defaultThemeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllThemesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultThemeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the themeList where title equals to UPDATED_TITLE
        defaultThemeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllThemesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where title is not null
        defaultThemeShouldBeFound("title.specified=true");

        // Get all the themeList where title is null
        defaultThemeShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllThemesByTitleContainsSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where title contains DEFAULT_TITLE
        defaultThemeShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the themeList where title contains UPDATED_TITLE
        defaultThemeShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllThemesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where title does not contain DEFAULT_TITLE
        defaultThemeShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the themeList where title does not contain UPDATED_TITLE
        defaultThemeShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllThemesByCourseIsEqualToSomething() throws Exception {
        Course course;
        if (TestUtil.findAll(em, Course.class).isEmpty()) {
            themeRepository.saveAndFlush(theme);
            course = CourseResourceIT.createEntity(em);
        } else {
            course = TestUtil.findAll(em, Course.class).get(0);
        }
        em.persist(course);
        em.flush();
        theme.setCourse(course);
        themeRepository.saveAndFlush(theme);
        Long courseId = course.getId();

        // Get all the themeList where course equals to courseId
        defaultThemeShouldBeFound("courseId.equals=" + courseId);

        // Get all the themeList where course equals to (courseId + 1)
        defaultThemeShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultThemeShouldBeFound(String filter) throws Exception {
        restThemeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theme.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));

        // Check, that the count call also returns 1
        restThemeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultThemeShouldNotBeFound(String filter) throws Exception {
        restThemeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restThemeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTheme() throws Exception {
        // Get the theme
        restThemeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        int databaseSizeBeforeUpdate = themeRepository.findAll().size();

        // Update the theme
        Theme updatedTheme = themeRepository.findById(theme.getId()).get();
        // Disconnect from session so that the updates on updatedTheme are not directly saved in db
        em.detach(updatedTheme);
        updatedTheme.title(UPDATED_TITLE);
        ThemeDTO themeDTO = themeMapper.toDto(updatedTheme);

        restThemeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
        Theme testTheme = themeList.get(themeList.size() - 1);
        assertThat(testTheme.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingTheme() throws Exception {
        int databaseSizeBeforeUpdate = themeRepository.findAll().size();
        theme.setId(count.incrementAndGet());

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTheme() throws Exception {
        int databaseSizeBeforeUpdate = themeRepository.findAll().size();
        theme.setId(count.incrementAndGet());

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTheme() throws Exception {
        int databaseSizeBeforeUpdate = themeRepository.findAll().size();
        theme.setId(count.incrementAndGet());

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateThemeWithPatch() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        int databaseSizeBeforeUpdate = themeRepository.findAll().size();

        // Update the theme using partial update
        Theme partialUpdatedTheme = new Theme();
        partialUpdatedTheme.setId(theme.getId());

        restThemeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTheme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTheme))
            )
            .andExpect(status().isOk());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
        Theme testTheme = themeList.get(themeList.size() - 1);
        assertThat(testTheme.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateThemeWithPatch() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        int databaseSizeBeforeUpdate = themeRepository.findAll().size();

        // Update the theme using partial update
        Theme partialUpdatedTheme = new Theme();
        partialUpdatedTheme.setId(theme.getId());

        partialUpdatedTheme.title(UPDATED_TITLE);

        restThemeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTheme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTheme))
            )
            .andExpect(status().isOk());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
        Theme testTheme = themeList.get(themeList.size() - 1);
        assertThat(testTheme.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingTheme() throws Exception {
        int databaseSizeBeforeUpdate = themeRepository.findAll().size();
        theme.setId(count.incrementAndGet());

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, themeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTheme() throws Exception {
        int databaseSizeBeforeUpdate = themeRepository.findAll().size();
        theme.setId(count.incrementAndGet());

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTheme() throws Exception {
        int databaseSizeBeforeUpdate = themeRepository.findAll().size();
        theme.setId(count.incrementAndGet());

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        int databaseSizeBeforeDelete = themeRepository.findAll().size();

        // Delete the theme
        restThemeMockMvc
            .perform(delete(ENTITY_API_URL_ID, theme.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
