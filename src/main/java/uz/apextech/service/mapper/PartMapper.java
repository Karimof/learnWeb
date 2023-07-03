package uz.apextech.service.mapper;

import org.mapstruct.*;
import uz.apextech.domain.Media;
import uz.apextech.domain.Part;
import uz.apextech.domain.Theme;
import uz.apextech.service.dto.MediaDTO;
import uz.apextech.service.dto.PartDTO;
import uz.apextech.service.dto.ThemeDTO;

/**
 * Mapper for the entity {@link Part} and its DTO {@link PartDTO}.
 */
@Mapper(componentModel = "spring")
public interface PartMapper extends EntityMapper<PartDTO, Part> {
    @Mapping(target = "theme", source = "theme")
    @Mapping(target = "media", source = "media")
    PartDTO toDto(Part s);

    @Named("themeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ThemeDTO toDtoThemeId(Theme theme);

    @Named("mediaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MediaDTO toDtoMediaId(Media media);
}
