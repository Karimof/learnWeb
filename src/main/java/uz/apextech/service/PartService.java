package uz.apextech.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.apextech.domain.Part;
import uz.apextech.repository.PartRepository;
import uz.apextech.service.dto.PartDTO;
import uz.apextech.service.mapper.PartMapper;

/**
 * Service Implementation for managing {@link Part}.
 */
@Service
@Transactional
public class PartService {

    private final Logger log = LoggerFactory.getLogger(PartService.class);

    private final PartRepository partRepository;

    private final PartMapper partMapper;

    public PartService(PartRepository partRepository, PartMapper partMapper) {
        this.partRepository = partRepository;
        this.partMapper = partMapper;
    }

    /**
     * Save a part.
     *
     * @param partDTO the entity to save.
     * @return the persisted entity.
     */
    public PartDTO save(PartDTO partDTO) {
        log.debug("Request to save Part : {}", partDTO);
        Part part = partMapper.toEntity(partDTO);
        part = partRepository.save(part);
        return partMapper.toDto(part);
    }

    /**
     * Update a part.
     *
     * @param partDTO the entity to save.
     * @return the persisted entity.
     */
    public PartDTO update(PartDTO partDTO) {
        log.debug("Request to update Part : {}", partDTO);
        Part part = partMapper.toEntity(partDTO);
        part = partRepository.save(part);
        return partMapper.toDto(part);
    }

    /**
     * Partially update a part.
     *
     * @param partDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartDTO> partialUpdate(PartDTO partDTO) {
        log.debug("Request to partially update Part : {}", partDTO);

        return partRepository
            .findById(partDTO.getId())
            .map(existingPart -> {
                partMapper.partialUpdate(existingPart, partDTO);

                return existingPart;
            })
            .map(partRepository::save)
            .map(partMapper::toDto);
    }

    /**
     * Get all the parts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parts");
        return partRepository.findAll(pageable).map(partMapper::toDto);
    }

    public List<PartDTO> findAllByTheme(Long themeId) {
        return partRepository.findAllByThemeId(themeId).stream().sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
            .map(partMapper::toDto)
            .collect(Collectors.toList());
    }


    /**
     * Get one part by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartDTO> findOne(Long id) {
        log.debug("Request to get Part : {}", id);
        return partRepository.findById(id).map(partMapper::toDto);
    }

    /**
     * Delete the part by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Part : {}", id);
        partRepository.deleteById(id);
    }
}
