package web.forum.imageservice.service.impl;

import com.mongodb.*;
import com.mongodb.client.gridfs.model.*;
import web.forum.imageservice.dto.*;
import web.forum.imageservice.service.*;
import lombok.*;
import org.apache.commons.io.*;
import org.bson.types.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.gridfs.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.time.*;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements IImageService {

    private final GridFsTemplate template;
    private final GridFsOperations operations;
    @Override
    public ImageDto save(MultipartFile multipartFile) {

        DBObject metadata = new BasicDBObject();
//        metadata.put("id", fileDto.getImageId());
        try {
            String fileName = multipartFile.getOriginalFilename();
            ObjectId fileID = template.store(
                    multipartFile.getInputStream(),
                    fileName,
                    multipartFile.getContentType(),
                    metadata);
            return ImageDto.builder()
                    .fileName(fileName)
                    .imageSize(multipartFile.getSize())
                    .uploadDate(LocalDate.now())
                    .imageId(fileID.toString())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ImageDto findById(String imageId) throws IOException {
        GridFSFile gridFSFile = template.findOne( new Query(Criteria.where("_id").is(imageId)) );
        ImageDto loadFile = new ImageDto();

        if ( gridFSFile.getMetadata() != null) {
            loadFile.setFileName( gridFSFile.getFilename() );

            loadFile.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );

            loadFile.setFile( IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()) );
        }

        return loadFile;
    }
}
