package uz.apextech.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.apextech.domain.Part;

/**
 * Spring Data JPA repository for the Part entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartRepository extends JpaRepository<Part, Long>, JpaSpecificationExecutor<Part> {
    List<Part> findAllByThemeId(Long themeId);

    List<Part> findAllBySubmenuIdAndBelongToSubsubIsNull(Long submenuId);

    List<Part> findAllBySubmenuIdAndSubSubmenuIdAndBelongToSubsub(Long submenuId, Long subSubmenuId, Long belongToSubsub);

    List<Part> findAllByThemeIdAndSubmenuIdAndBelongToSubsubIsNotNull(Long themeId, Long submenuId);
}
