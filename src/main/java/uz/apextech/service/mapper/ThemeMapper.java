package uz.apextech.service.mapper;

import org.mapstruct.*;
import uz.apextech.domain.Course;
import uz.apextech.domain.Theme;
import uz.apextech.service.dto.CourseDTO;
import uz.apextech.service.dto.ThemeDTO;

/**
 * Mapper for the entity {@link Theme} and its DTO {@link ThemeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ThemeMapper extends EntityMapper<ThemeDTO, Theme> {
    @Mapping(target = "course", source = "course", qualifiedByName = "courseId")
    ThemeDTO toDto(Theme s);

    @Named("courseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoCourseId(Course course);
}
