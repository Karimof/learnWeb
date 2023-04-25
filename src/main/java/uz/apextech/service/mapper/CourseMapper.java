package uz.apextech.service.mapper;

import org.mapstruct.*;
import uz.apextech.domain.Course;
import uz.apextech.service.dto.CourseDTO;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {}
