package web.forum.imageservice.mapper;

import com.mongodb.*;
import org.mapstruct.*;
import web.forum.imageservice.dto.*;
import web.forum.imageservice.model.*;

@Mapper(componentModel = "spring")
public interface MetaDataMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "filename", source = "filename"),
            @Mapping(target = "contentType",source = "metadata", qualifiedByName  = "content"),
            @Mapping(target = "uploadDate", source = "uploadDate"),
            @Mapping(target = "imageSize", source = "chunkSize")
    })
    ImageDto toDto(FileMetaData fileMetaData);
    @Named("content")
   default String getContentType(DBObject fileMetaData){
        return (String) fileMetaData.get("_contentType");
    }

}
