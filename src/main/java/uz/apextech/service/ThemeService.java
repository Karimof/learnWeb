package uz.apextech.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.apextech.domain.Theme;
import uz.apextech.repository.ThemeRepository;
import uz.apextech.service.dto.ThemeDTO;
import uz.apextech.service.mapper.ThemeMapper;

/**
 * Service Implementation for managing {@link Theme}.
 */
@Service
@Transactional
public class ThemeService {

    private final Logger log = LoggerFactory.getLogger(ThemeService.class);

    private final ThemeRepository themeRepository;

    private final ThemeMapper themeMapper;

    public ThemeService(ThemeRepository themeRepository, ThemeMapper themeMapper) {
        this.themeRepository = themeRepository;
        this.themeMapper = themeMapper;
    }

    /**
     * Save a theme.
     *
     * @param themeDTO the entity to save.
     * @return the persisted entity.
     */
    public ThemeDTO save(ThemeDTO themeDTO) {
        log.debug("Request to save Theme : {}", themeDTO);
        Theme theme = themeMapper.toEntity(themeDTO);
        theme = themeRepository.save(theme);
        return themeMapper.toDto(theme);
    }

    /**
     * Update a theme.
     *
     * @param themeDTO the entity to save.
     * @return the persisted entity.
     */
    public ThemeDTO update(ThemeDTO themeDTO) {
        log.debug("Request to update Theme : {}", themeDTO);
        Theme theme = themeMapper.toEntity(themeDTO);
        theme = themeRepository.save(theme);
        return themeMapper.toDto(theme);
    }

    /**
     * Partially update a theme.
     *
     * @param themeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ThemeDTO> partialUpdate(ThemeDTO themeDTO) {
        log.debug("Request to partially update Theme : {}", themeDTO);

        return themeRepository
            .findById(themeDTO.getId())
            .map(existingTheme -> {
                themeMapper.partialUpdate(existingTheme, themeDTO);

                return existingTheme;
            })
            .map(themeRepository::save)
            .map(themeMapper::toDto);
    }

    /**
     * Get all the themes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ThemeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Themes");
        return themeRepository.findAll(pageable).map(themeMapper::toDto);
    }

    /**
     * Get one theme by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ThemeDTO> findOne(Long id) {
        log.debug("Request to get Theme : {}", id);
        return themeRepository.findById(id).map(themeMapper::toDto);
    }

    /**
     * Delete the theme by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Theme : {}", id);
        themeRepository.deleteById(id);
    }
}
