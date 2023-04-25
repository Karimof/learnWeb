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
import uz.apextech.domain.Part;
import uz.apextech.repository.PartRepository;
import uz.apextech.service.criteria.PartCriteria;
import uz.apextech.service.dto.PartDTO;
import uz.apextech.service.mapper.PartMapper;

/**
 * Service for executing complex queries for {@link Part} entities in the database.
 * The main input is a {@link PartCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartDTO} or a {@link Page} of {@link PartDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartQueryService extends QueryService<Part> {

    private final Logger log = LoggerFactory.getLogger(PartQueryService.class);

    private final PartRepository partRepository;

    private final PartMapper partMapper;

    public PartQueryService(PartRepository partRepository, PartMapper partMapper) {
        this.partRepository = partRepository;
        this.partMapper = partMapper;
    }

    /**
     * Return a {@link List} of {@link PartDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartDTO> findByCriteria(PartCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Part> specification = createSpecification(criteria);
        return partMapper.toDto(partRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PartDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartDTO> findByCriteria(PartCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Part> specification = createSpecification(criteria);
        return partRepository.findAll(specification, page).map(partMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Part> specification = createSpecification(criteria);
        return partRepository.count(specification);
    }

    /**
     * Function to convert {@link PartCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Part> createSpecification(PartCriteria criteria) {
        Specification<Part> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Part_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Part_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Part_.description));
            }
            if (criteria.getQuestion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuestion(), Part_.question));
            }
            if (criteria.getCodeTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeTitle(), Part_.codeTitle));
            }
            if (criteria.getCodeDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeDescription(), Part_.codeDescription));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Part_.code));
            }
            if (criteria.getFullCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullCode(), Part_.fullCode));
            }
            if (criteria.getResult() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResult(), Part_.result));
            }
            if (criteria.getAdditional() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdditional(), Part_.additional));
            }
            if (criteria.getThemeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getThemeId(), root -> root.join(Part_.theme, JoinType.LEFT).get(Theme_.id))
                    );
            }
            if (criteria.getMediaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMediaId(), root -> root.join(Part_.media, JoinType.LEFT).get(Media_.id))
                    );
            }
        }
        return specification;
    }
}
