package uz.apextech.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.apextech.domain.Part;

import java.util.List;

/**
 * Spring Data JPA repository for the Part entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartRepository extends JpaRepository<Part, Long>, JpaSpecificationExecutor<Part> {

    List<Part> findAllByThemeId(Long themeId);
}
