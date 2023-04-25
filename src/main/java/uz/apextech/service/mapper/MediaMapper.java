package uz.apextech.service.mapper;

import org.mapstruct.*;
import uz.apextech.domain.Media;
import uz.apextech.domain.Theme;
import uz.apextech.service.dto.MediaDTO;
import uz.apextech.service.dto.ThemeDTO;

/**
 * Mapper for the entity {@link Media} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MediaMapper extends EntityMapper<MediaDTO, Media> {
    @Mapping(target = "theme", source = "theme", qualifiedByName = "themeId")
    MediaDTO toDto(Media s);

    @Named("themeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ThemeDTO toDtoThemeId(Theme theme);
}
