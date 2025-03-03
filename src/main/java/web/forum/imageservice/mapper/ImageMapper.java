package web.forum.imageservice.mapper;

import org.mapstruct.*;
import web.forum.imageservice.dto.*;
import web.forum.imageservice.model.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "filename", source = "filename"),
            @Mapping(target = "uploadDate", source = "uploadDate"),
            @Mapping(target = "imageSize", source = "imageSize"),
            @Mapping(target = "targetId", source = "targetId"),
            @Mapping(target = "contentType", source = "contentType"),
            @Mapping(target = "metaName", source = "metaName"),
    })
    ImageDto toDto(ImageCache imageCache);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "filename", source = "filename"),
            @Mapping(target = "uploadDate", source = "uploadDate"),
            @Mapping(target = "imageSize", source = "imageSize"),
            @Mapping(target = "targetId", source = "targetId"),
            @Mapping(target = "contentType", source = "contentType"),
            @Mapping(target = "metaName", source = "metaName"),
    })
    ImageCache fromDto(ImageDto imageDto);
}
