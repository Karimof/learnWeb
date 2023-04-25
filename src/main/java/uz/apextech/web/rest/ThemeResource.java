package uz.apextech.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.apextech.domain.Theme;
import uz.apextech.repository.ThemeRepository;
import uz.apextech.service.ThemeQueryService;
import uz.apextech.service.ThemeService;
import uz.apextech.service.criteria.ThemeCriteria;
import uz.apextech.service.dto.ThemeDTO;
import uz.apextech.service.mapper.ThemeMapper;
import uz.apextech.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.apextech.domain.Theme}.
 */
@RestController
@RequestMapping("/api")
public class ThemeResource {

    private final Logger log = LoggerFactory.getLogger(ThemeResource.class);

    private static final String ENTITY_NAME = "theme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeService themeService;

    private final ThemeRepository themeRepository;

    private final ThemeQueryService themeQueryService;

    private final ThemeMapper themeMapper;

    public ThemeResource(ThemeService themeService,
                         ThemeRepository themeRepository,
                         ThemeQueryService themeQueryService,
                         ThemeMapper themeMapper) {
        this.themeService = themeService;
        this.themeRepository = themeRepository;
        this.themeQueryService = themeQueryService;
        this.themeMapper = themeMapper;
    }

    /**
     * {@code POST  /themes} : Create a new theme.
     *
     * @param themeDTO the themeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeDTO, or with status {@code 400 (Bad Request)} if the theme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/themes")
    public ResponseEntity<ThemeDTO> createTheme(@Valid @RequestBody ThemeDTO themeDTO) throws URISyntaxException {
        log.debug("REST request to save Theme : {}", themeDTO);
        if (themeDTO.getId() != null) {
            throw new BadRequestAlertException("A new theme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThemeDTO result = themeService.save(themeDTO);
        return ResponseEntity
            .created(new URI("/api/themes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /themes/:id} : Updates an existing theme.
     *
     * @param id       the id of the themeDTO to save.
     * @param themeDTO the themeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeDTO,
     * or with status {@code 400 (Bad Request)} if the themeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/themes/{id}")
    public ResponseEntity<ThemeDTO> updateTheme(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ThemeDTO themeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Theme : {}, {}", id, themeDTO);
        if (themeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ThemeDTO result = themeService.update(themeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /themes/:id} : Partial updates given fields of an existing theme, field will ignore if it is null
     *
     * @param id       the id of the themeDTO to save.
     * @param themeDTO the themeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeDTO,
     * or with status {@code 400 (Bad Request)} if the themeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the themeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the themeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/themes/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<ThemeDTO> partialUpdateTheme(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ThemeDTO themeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Theme partially : {}, {}", id, themeDTO);
        if (themeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThemeDTO> result = themeService.partialUpdate(themeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /themes} : get all the themes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themes in body.
     */

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeDTO>> getAllThemes(
        ThemeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Themes by criteria: {}", criteria);
        Page<ThemeDTO> page = themeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @CrossOrigin("http://localhost:5738/")
    @GetMapping("/themes/sorted")
    public ResponseEntity<Map<Long, List<ThemeDTO>>> getThemes() {
        List<ThemeDTO> list = themeRepository.findAll().stream()
            .map(themeMapper::toDto).collect(Collectors.toList());
        Map<Long, List<ThemeDTO>> themeMap = list.stream().collect(Collectors.groupingBy(ThemeDTO::getId));
        return ResponseEntity.ok().body(themeMap);
    }

    /**
     * {@code GET  /themes/count} : count all the themes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/themes/count")
    public ResponseEntity<Long> countThemes(ThemeCriteria criteria) {
        log.debug("REST request to count Themes by criteria: {}", criteria);
        return ResponseEntity.ok().body(themeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /themes/:id} : get the "id" theme.
     *
     * @param id the id of the themeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/themes/{id}")
    public ResponseEntity<ThemeDTO> getTheme(@PathVariable Long id) {
        log.debug("REST request to get Theme : {}", id);
        Optional<ThemeDTO> themeDTO = themeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(themeDTO);
    }

    /**
     * {@code DELETE  /themes/:id} : delete the "id" theme.
     *
     * @param id the id of the themeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        log.debug("REST request to delete Theme : {}", id);
        themeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
