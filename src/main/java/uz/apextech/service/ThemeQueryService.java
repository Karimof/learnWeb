package uz.apextech.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.apextech.domain.*; // for static metamodels
import uz.apextech.domain.Theme;
import uz.apextech.repository.ThemeRepository;
import uz.apextech.service.criteria.ThemeCriteria;
import uz.apextech.service.dto.ThemeDTO;
import uz.apextech.service.mapper.ThemeMapper;

/**
 * Service for executing complex queries for {@link Theme} entities in the database.
 * The main input is a {@link ThemeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ThemeDTO} or a {@link Page} of {@link ThemeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ThemeQueryService extends QueryService<Theme> {

    private final Logger log = LoggerFactory.getLogger(ThemeQueryService.class);

    private final ThemeRepository themeRepository;

    private final ThemeMapper themeMapper;

    public ThemeQueryService(ThemeRepository themeRepository, ThemeMapper themeMapper) {
        this.themeRepository = themeRepository;
        this.themeMapper = themeMapper;
    }

    /**
     * Return a {@link List} of {@link ThemeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ThemeDTO> findByCriteria(ThemeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Theme> specification = createSpecification(criteria);
        return themeMapper.toDto(themeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ThemeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ThemeDTO> findByCriteria(ThemeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Theme> specification = createSpecification(criteria);
        return themeRepository.findAll(specification, page).map(themeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ThemeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Theme> specification = createSpecification(criteria);
        return themeRepository.count(specification);
    }

    /**
     * Function to convert {@link ThemeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Theme> createSpecification(ThemeCriteria criteria) {
        Specification<Theme> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Theme_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Theme_.title));
            }
            if (criteria.getCourseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCourseId(), root -> root.join(Theme_.course, JoinType.LEFT).get(Course_.id))
                    );
            }
        }
        return specification;
    }
}
