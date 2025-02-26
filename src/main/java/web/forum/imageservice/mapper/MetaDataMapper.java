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
            @Mapping(target = "imageSize", source = "chunkSize"),
            @Mapping(target = "targetId", source = "metadata", qualifiedByName = "targetId"),
            @Mapping(target = "metaName",source = "metadata", qualifiedByName  = "imageName"),
    })
    ImageDto toDto(FileMetaData fileMetaData);

    @Named("targetId")
    default String getTargetId(DBObject fileMetaData){
        return (String)fileMetaData.get("targetId");
    }
    @Named("content")
   default String getContentType(DBObject fileMetaData){
        return (String) fileMetaData.get("_contentType");
    }

    @Named("imageName")
    default String getImageName(DBObject fileMetaData){
        return (String) fileMetaData.get("imageName");
    }

}
