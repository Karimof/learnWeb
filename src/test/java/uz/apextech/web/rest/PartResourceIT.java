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
import uz.apextech.domain.Media;
import uz.apextech.domain.Part;
import uz.apextech.domain.Theme;
import uz.apextech.repository.PartRepository;
import uz.apextech.service.criteria.PartCriteria;
import uz.apextech.service.dto.PartDTO;
import uz.apextech.service.mapper.PartMapper;

/**
 * Integration tests for the {@link PartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FULL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartMapper partMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartMockMvc;

    private Part part;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Part createEntity(EntityManager em) {
        Part part = new Part()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .question(DEFAULT_QUESTION)
            .codeTitle(DEFAULT_CODE_TITLE)
            .codeDescription(DEFAULT_CODE_DESCRIPTION)
            .code(DEFAULT_CODE)
            .fullCode(DEFAULT_FULL_CODE)
            .result(DEFAULT_RESULT)
            .additional(DEFAULT_ADDITIONAL);
        return part;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Part createUpdatedEntity(EntityManager em) {
        Part part = new Part()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .question(UPDATED_QUESTION)
            .codeTitle(UPDATED_CODE_TITLE)
            .codeDescription(UPDATED_CODE_DESCRIPTION)
            .code(UPDATED_CODE)
            .fullCode(UPDATED_FULL_CODE)
            .result(UPDATED_RESULT)
            .additional(UPDATED_ADDITIONAL);
        return part;
    }

    @BeforeEach
    public void initTest() {
        part = createEntity(em);
    }

    @Test
    @Transactional
    void createPart() throws Exception {
        int databaseSizeBeforeCreate = partRepository.findAll().size();
        // Create the Part
        PartDTO partDTO = partMapper.toDto(part);
        restPartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partDTO)))
            .andExpect(status().isCreated());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeCreate + 1);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPart.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testPart.getCodeTitle()).isEqualTo(DEFAULT_CODE_TITLE);
        assertThat(testPart.getCodeDescription()).isEqualTo(DEFAULT_CODE_DESCRIPTION);
        assertThat(testPart.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPart.getFullCode()).isEqualTo(DEFAULT_FULL_CODE);
        assertThat(testPart.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testPart.getAdditional()).isEqualTo(DEFAULT_ADDITIONAL);
    }

    @Test
    @Transactional
    void createPartWithExistingId() throws Exception {
        // Create the Part with an existing ID
        part.setId(1L);
        PartDTO partDTO = partMapper.toDto(part);

        int databaseSizeBeforeCreate = partRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partRepository.findAll().size();
        // set the field null
        part.setTitle(null);

        // Create the Part, which fails.
        PartDTO partDTO = partMapper.toDto(part);

        restPartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partDTO)))
            .andExpect(status().isBadRequest());

        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParts() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList
        restPartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(part.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].codeTitle").value(hasItem(DEFAULT_CODE_TITLE)))
            .andExpect(jsonPath("$.[*].codeDescription").value(hasItem(DEFAULT_CODE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].fullCode").value(hasItem(DEFAULT_FULL_CODE)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].additional").value(hasItem(DEFAULT_ADDITIONAL)));
    }

    @Test
    @Transactional
    void getPart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get the part
        restPartMockMvc
            .perform(get(ENTITY_API_URL_ID, part.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(part.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.codeTitle").value(DEFAULT_CODE_TITLE))
            .andExpect(jsonPath("$.codeDescription").value(DEFAULT_CODE_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.fullCode").value(DEFAULT_FULL_CODE))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT))
            .andExpect(jsonPath("$.additional").value(DEFAULT_ADDITIONAL));
    }

    @Test
    @Transactional
    void getPartsByIdFiltering() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        Long id = part.getId();

        defaultPartShouldBeFound("id.equals=" + id);
        defaultPartShouldNotBeFound("id.notEquals=" + id);

        defaultPartShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPartShouldNotBeFound("id.greaterThan=" + id);

        defaultPartShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPartShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where title equals to DEFAULT_TITLE
        defaultPartShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the partList where title equals to UPDATED_TITLE
        defaultPartShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPartShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the partList where title equals to UPDATED_TITLE
        defaultPartShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where title is not null
        defaultPartShouldBeFound("title.specified=true");

        // Get all the partList where title is null
        defaultPartShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByTitleContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where title contains DEFAULT_TITLE
        defaultPartShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the partList where title contains UPDATED_TITLE
        defaultPartShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where title does not contain DEFAULT_TITLE
        defaultPartShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the partList where title does not contain UPDATED_TITLE
        defaultPartShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where description equals to DEFAULT_DESCRIPTION
        defaultPartShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the partList where description equals to UPDATED_DESCRIPTION
        defaultPartShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPartShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the partList where description equals to UPDATED_DESCRIPTION
        defaultPartShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where description is not null
        defaultPartShouldBeFound("description.specified=true");

        // Get all the partList where description is null
        defaultPartShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where description contains DEFAULT_DESCRIPTION
        defaultPartShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the partList where description contains UPDATED_DESCRIPTION
        defaultPartShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where description does not contain DEFAULT_DESCRIPTION
        defaultPartShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the partList where description does not contain UPDATED_DESCRIPTION
        defaultPartShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where question equals to DEFAULT_QUESTION
        defaultPartShouldBeFound("question.equals=" + DEFAULT_QUESTION);

        // Get all the partList where question equals to UPDATED_QUESTION
        defaultPartShouldNotBeFound("question.equals=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllPartsByQuestionIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where question in DEFAULT_QUESTION or UPDATED_QUESTION
        defaultPartShouldBeFound("question.in=" + DEFAULT_QUESTION + "," + UPDATED_QUESTION);

        // Get all the partList where question equals to UPDATED_QUESTION
        defaultPartShouldNotBeFound("question.in=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllPartsByQuestionIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where question is not null
        defaultPartShouldBeFound("question.specified=true");

        // Get all the partList where question is null
        defaultPartShouldNotBeFound("question.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByQuestionContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where question contains DEFAULT_QUESTION
        defaultPartShouldBeFound("question.contains=" + DEFAULT_QUESTION);

        // Get all the partList where question contains UPDATED_QUESTION
        defaultPartShouldNotBeFound("question.contains=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllPartsByQuestionNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where question does not contain DEFAULT_QUESTION
        defaultPartShouldNotBeFound("question.doesNotContain=" + DEFAULT_QUESTION);

        // Get all the partList where question does not contain UPDATED_QUESTION
        defaultPartShouldBeFound("question.doesNotContain=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllPartsByCodeTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeTitle equals to DEFAULT_CODE_TITLE
        defaultPartShouldBeFound("codeTitle.equals=" + DEFAULT_CODE_TITLE);

        // Get all the partList where codeTitle equals to UPDATED_CODE_TITLE
        defaultPartShouldNotBeFound("codeTitle.equals=" + UPDATED_CODE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartsByCodeTitleIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeTitle in DEFAULT_CODE_TITLE or UPDATED_CODE_TITLE
        defaultPartShouldBeFound("codeTitle.in=" + DEFAULT_CODE_TITLE + "," + UPDATED_CODE_TITLE);

        // Get all the partList where codeTitle equals to UPDATED_CODE_TITLE
        defaultPartShouldNotBeFound("codeTitle.in=" + UPDATED_CODE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartsByCodeTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeTitle is not null
        defaultPartShouldBeFound("codeTitle.specified=true");

        // Get all the partList where codeTitle is null
        defaultPartShouldNotBeFound("codeTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByCodeTitleContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeTitle contains DEFAULT_CODE_TITLE
        defaultPartShouldBeFound("codeTitle.contains=" + DEFAULT_CODE_TITLE);

        // Get all the partList where codeTitle contains UPDATED_CODE_TITLE
        defaultPartShouldNotBeFound("codeTitle.contains=" + UPDATED_CODE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartsByCodeTitleNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeTitle does not contain DEFAULT_CODE_TITLE
        defaultPartShouldNotBeFound("codeTitle.doesNotContain=" + DEFAULT_CODE_TITLE);

        // Get all the partList where codeTitle does not contain UPDATED_CODE_TITLE
        defaultPartShouldBeFound("codeTitle.doesNotContain=" + UPDATED_CODE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartsByCodeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeDescription equals to DEFAULT_CODE_DESCRIPTION
        defaultPartShouldBeFound("codeDescription.equals=" + DEFAULT_CODE_DESCRIPTION);

        // Get all the partList where codeDescription equals to UPDATED_CODE_DESCRIPTION
        defaultPartShouldNotBeFound("codeDescription.equals=" + UPDATED_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartsByCodeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeDescription in DEFAULT_CODE_DESCRIPTION or UPDATED_CODE_DESCRIPTION
        defaultPartShouldBeFound("codeDescription.in=" + DEFAULT_CODE_DESCRIPTION + "," + UPDATED_CODE_DESCRIPTION);

        // Get all the partList where codeDescription equals to UPDATED_CODE_DESCRIPTION
        defaultPartShouldNotBeFound("codeDescription.in=" + UPDATED_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartsByCodeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeDescription is not null
        defaultPartShouldBeFound("codeDescription.specified=true");

        // Get all the partList where codeDescription is null
        defaultPartShouldNotBeFound("codeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByCodeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeDescription contains DEFAULT_CODE_DESCRIPTION
        defaultPartShouldBeFound("codeDescription.contains=" + DEFAULT_CODE_DESCRIPTION);

        // Get all the partList where codeDescription contains UPDATED_CODE_DESCRIPTION
        defaultPartShouldNotBeFound("codeDescription.contains=" + UPDATED_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartsByCodeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where codeDescription does not contain DEFAULT_CODE_DESCRIPTION
        defaultPartShouldNotBeFound("codeDescription.doesNotContain=" + DEFAULT_CODE_DESCRIPTION);

        // Get all the partList where codeDescription does not contain UPDATED_CODE_DESCRIPTION
        defaultPartShouldBeFound("codeDescription.doesNotContain=" + UPDATED_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where code equals to DEFAULT_CODE
        defaultPartShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the partList where code equals to UPDATED_CODE
        defaultPartShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPartsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPartShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the partList where code equals to UPDATED_CODE
        defaultPartShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPartsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where code is not null
        defaultPartShouldBeFound("code.specified=true");

        // Get all the partList where code is null
        defaultPartShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByCodeContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where code contains DEFAULT_CODE
        defaultPartShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the partList where code contains UPDATED_CODE
        defaultPartShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPartsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where code does not contain DEFAULT_CODE
        defaultPartShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the partList where code does not contain UPDATED_CODE
        defaultPartShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPartsByFullCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where fullCode equals to DEFAULT_FULL_CODE
        defaultPartShouldBeFound("fullCode.equals=" + DEFAULT_FULL_CODE);

        // Get all the partList where fullCode equals to UPDATED_FULL_CODE
        defaultPartShouldNotBeFound("fullCode.equals=" + UPDATED_FULL_CODE);
    }

    @Test
    @Transactional
    void getAllPartsByFullCodeIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where fullCode in DEFAULT_FULL_CODE or UPDATED_FULL_CODE
        defaultPartShouldBeFound("fullCode.in=" + DEFAULT_FULL_CODE + "," + UPDATED_FULL_CODE);

        // Get all the partList where fullCode equals to UPDATED_FULL_CODE
        defaultPartShouldNotBeFound("fullCode.in=" + UPDATED_FULL_CODE);
    }

    @Test
    @Transactional
    void getAllPartsByFullCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where fullCode is not null
        defaultPartShouldBeFound("fullCode.specified=true");

        // Get all the partList where fullCode is null
        defaultPartShouldNotBeFound("fullCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByFullCodeContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where fullCode contains DEFAULT_FULL_CODE
        defaultPartShouldBeFound("fullCode.contains=" + DEFAULT_FULL_CODE);

        // Get all the partList where fullCode contains UPDATED_FULL_CODE
        defaultPartShouldNotBeFound("fullCode.contains=" + UPDATED_FULL_CODE);
    }

    @Test
    @Transactional
    void getAllPartsByFullCodeNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where fullCode does not contain DEFAULT_FULL_CODE
        defaultPartShouldNotBeFound("fullCode.doesNotContain=" + DEFAULT_FULL_CODE);

        // Get all the partList where fullCode does not contain UPDATED_FULL_CODE
        defaultPartShouldBeFound("fullCode.doesNotContain=" + UPDATED_FULL_CODE);
    }

    @Test
    @Transactional
    void getAllPartsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where result equals to DEFAULT_RESULT
        defaultPartShouldBeFound("result.equals=" + DEFAULT_RESULT);

        // Get all the partList where result equals to UPDATED_RESULT
        defaultPartShouldNotBeFound("result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllPartsByResultIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where result in DEFAULT_RESULT or UPDATED_RESULT
        defaultPartShouldBeFound("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT);

        // Get all the partList where result equals to UPDATED_RESULT
        defaultPartShouldNotBeFound("result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllPartsByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where result is not null
        defaultPartShouldBeFound("result.specified=true");

        // Get all the partList where result is null
        defaultPartShouldNotBeFound("result.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByResultContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where result contains DEFAULT_RESULT
        defaultPartShouldBeFound("result.contains=" + DEFAULT_RESULT);

        // Get all the partList where result contains UPDATED_RESULT
        defaultPartShouldNotBeFound("result.contains=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllPartsByResultNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where result does not contain DEFAULT_RESULT
        defaultPartShouldNotBeFound("result.doesNotContain=" + DEFAULT_RESULT);

        // Get all the partList where result does not contain UPDATED_RESULT
        defaultPartShouldBeFound("result.doesNotContain=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllPartsByAdditionalIsEqualToSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where additional equals to DEFAULT_ADDITIONAL
        defaultPartShouldBeFound("additional.equals=" + DEFAULT_ADDITIONAL);

        // Get all the partList where additional equals to UPDATED_ADDITIONAL
        defaultPartShouldNotBeFound("additional.equals=" + UPDATED_ADDITIONAL);
    }

    @Test
    @Transactional
    void getAllPartsByAdditionalIsInShouldWork() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where additional in DEFAULT_ADDITIONAL or UPDATED_ADDITIONAL
        defaultPartShouldBeFound("additional.in=" + DEFAULT_ADDITIONAL + "," + UPDATED_ADDITIONAL);

        // Get all the partList where additional equals to UPDATED_ADDITIONAL
        defaultPartShouldNotBeFound("additional.in=" + UPDATED_ADDITIONAL);
    }

    @Test
    @Transactional
    void getAllPartsByAdditionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where additional is not null
        defaultPartShouldBeFound("additional.specified=true");

        // Get all the partList where additional is null
        defaultPartShouldNotBeFound("additional.specified=false");
    }

    @Test
    @Transactional
    void getAllPartsByAdditionalContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where additional contains DEFAULT_ADDITIONAL
        defaultPartShouldBeFound("additional.contains=" + DEFAULT_ADDITIONAL);

        // Get all the partList where additional contains UPDATED_ADDITIONAL
        defaultPartShouldNotBeFound("additional.contains=" + UPDATED_ADDITIONAL);
    }

    @Test
    @Transactional
    void getAllPartsByAdditionalNotContainsSomething() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList where additional does not contain DEFAULT_ADDITIONAL
        defaultPartShouldNotBeFound("additional.doesNotContain=" + DEFAULT_ADDITIONAL);

        // Get all the partList where additional does not contain UPDATED_ADDITIONAL
        defaultPartShouldBeFound("additional.doesNotContain=" + UPDATED_ADDITIONAL);
    }

    @Test
    @Transactional
    void getAllPartsByThemeIsEqualToSomething() throws Exception {
        Theme theme;
        if (TestUtil.findAll(em, Theme.class).isEmpty()) {
            partRepository.saveAndFlush(part);
            theme = ThemeResourceIT.createEntity(em);
        } else {
            theme = TestUtil.findAll(em, Theme.class).get(0);
        }
        em.persist(theme);
        em.flush();
        part.setTheme(theme);
        partRepository.saveAndFlush(part);
        Long themeId = theme.getId();

        // Get all the partList where theme equals to themeId
        defaultPartShouldBeFound("themeId.equals=" + themeId);

        // Get all the partList where theme equals to (themeId + 1)
        defaultPartShouldNotBeFound("themeId.equals=" + (themeId + 1));
    }

    @Test
    @Transactional
    void getAllPartsByMediaIsEqualToSomething() throws Exception {
        Media media;
        if (TestUtil.findAll(em, Media.class).isEmpty()) {
            partRepository.saveAndFlush(part);
            media = MediaResourceIT.createEntity(em);
        } else {
            media = TestUtil.findAll(em, Media.class).get(0);
        }
        em.persist(media);
        em.flush();
        part.setMedia(media);
        partRepository.saveAndFlush(part);
        Long mediaId = media.getId();

        // Get all the partList where media equals to mediaId
        defaultPartShouldBeFound("mediaId.equals=" + mediaId);

        // Get all the partList where media equals to (mediaId + 1)
        defaultPartShouldNotBeFound("mediaId.equals=" + (mediaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartShouldBeFound(String filter) throws Exception {
        restPartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(part.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].codeTitle").value(hasItem(DEFAULT_CODE_TITLE)))
            .andExpect(jsonPath("$.[*].codeDescription").value(hasItem(DEFAULT_CODE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].fullCode").value(hasItem(DEFAULT_FULL_CODE)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].additional").value(hasItem(DEFAULT_ADDITIONAL)));

        // Check, that the count call also returns 1
        restPartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartShouldNotBeFound(String filter) throws Exception {
        restPartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPart() throws Exception {
        // Get the part
        restPartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Update the part
        Part updatedPart = partRepository.findById(part.getId()).get();
        // Disconnect from session so that the updates on updatedPart are not directly saved in db
        em.detach(updatedPart);
        updatedPart
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .question(UPDATED_QUESTION)
            .codeTitle(UPDATED_CODE_TITLE)
            .codeDescription(UPDATED_CODE_DESCRIPTION)
            .code(UPDATED_CODE)
            .fullCode(UPDATED_FULL_CODE)
            .result(UPDATED_RESULT)
            .additional(UPDATED_ADDITIONAL);
        PartDTO partDTO = partMapper.toDto(updatedPart);

        restPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partDTO))
            )
            .andExpect(status().isOk());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPart.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPart.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testPart.getCodeTitle()).isEqualTo(UPDATED_CODE_TITLE);
        assertThat(testPart.getCodeDescription()).isEqualTo(UPDATED_CODE_DESCRIPTION);
        assertThat(testPart.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPart.getFullCode()).isEqualTo(UPDATED_FULL_CODE);
        assertThat(testPart.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testPart.getAdditional()).isEqualTo(UPDATED_ADDITIONAL);
    }

    @Test
    @Transactional
    void putNonExistingPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // Create the Part
        PartDTO partDTO = partMapper.toDto(part);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // Create the Part
        PartDTO partDTO = partMapper.toDto(part);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // Create the Part
        PartDTO partDTO = partMapper.toDto(part);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartWithPatch() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Update the part using partial update
        Part partialUpdatedPart = new Part();
        partialUpdatedPart.setId(part.getId());

        partialUpdatedPart
            .codeTitle(UPDATED_CODE_TITLE)
            .codeDescription(UPDATED_CODE_DESCRIPTION)
            .code(UPDATED_CODE)
            .fullCode(UPDATED_FULL_CODE)
            .additional(UPDATED_ADDITIONAL);

        restPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPart))
            )
            .andExpect(status().isOk());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPart.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testPart.getCodeTitle()).isEqualTo(UPDATED_CODE_TITLE);
        assertThat(testPart.getCodeDescription()).isEqualTo(UPDATED_CODE_DESCRIPTION);
        assertThat(testPart.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPart.getFullCode()).isEqualTo(UPDATED_FULL_CODE);
        assertThat(testPart.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testPart.getAdditional()).isEqualTo(UPDATED_ADDITIONAL);
    }

    @Test
    @Transactional
    void fullUpdatePartWithPatch() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Update the part using partial update
        Part partialUpdatedPart = new Part();
        partialUpdatedPart.setId(part.getId());

        partialUpdatedPart
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .question(UPDATED_QUESTION)
            .codeTitle(UPDATED_CODE_TITLE)
            .codeDescription(UPDATED_CODE_DESCRIPTION)
            .code(UPDATED_CODE)
            .fullCode(UPDATED_FULL_CODE)
            .result(UPDATED_RESULT)
            .additional(UPDATED_ADDITIONAL);

        restPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPart))
            )
            .andExpect(status().isOk());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPart.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPart.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testPart.getCodeTitle()).isEqualTo(UPDATED_CODE_TITLE);
        assertThat(testPart.getCodeDescription()).isEqualTo(UPDATED_CODE_DESCRIPTION);
        assertThat(testPart.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPart.getFullCode()).isEqualTo(UPDATED_FULL_CODE);
        assertThat(testPart.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testPart.getAdditional()).isEqualTo(UPDATED_ADDITIONAL);
    }

    @Test
    @Transactional
    void patchNonExistingPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // Create the Part
        PartDTO partDTO = partMapper.toDto(part);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // Create the Part
        PartDTO partDTO = partMapper.toDto(part);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // Create the Part
        PartDTO partDTO = partMapper.toDto(part);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(partDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeDelete = partRepository.findAll().size();

        // Delete the part
        restPartMockMvc
            .perform(delete(ENTITY_API_URL_ID, part.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
