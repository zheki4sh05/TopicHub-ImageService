package web.forum.imageservice.service.impl;

import com.mongodb.*;
import com.mongodb.client.gridfs.model.*;
import lombok.*;
import org.apache.commons.io.*;
import org.bson.types.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.gridfs.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;
import web.forum.imageservice.dto.*;
import web.forum.imageservice.mapper.*;
import web.forum.imageservice.model.*;
import web.forum.imageservice.service.*;

import java.io.*;
import java.time.*;
import java.util.*;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements IImageService {

    private final GridFsTemplate template;
    private final GridFsOperations operations;
    private final MongoTemplate mongoTemplate;
    private final MetaDataMapper metaDataMapper;

    @Override
    public ImageDto save(MultipartFile multipartFile) {
        DBObject metadata = new BasicDBObject();
        try {
            String fileName = multipartFile.getOriginalFilename();
            ObjectId fileID = template.store(
                    multipartFile.getInputStream(),
                    fileName,
                    multipartFile.getContentType(),
                    metadata);
            return ImageDto.builder()
                    .filename(fileName)
                    .imageSize(multipartFile.getSize())
                    .uploadDate(LocalDate.now())
                    .id(fileID.toString())
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
            loadFile.setFilename( gridFSFile.getFilename() );

            loadFile.setContentType( gridFSFile.getMetadata().get("_contentType").toString() );

            loadFile.setFile( IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()) );
        }

        return loadFile;
    }

    @Override
    public PageResponse<ImageDto> fetch(Integer page) {
        Query query = new Query();
        int PAGE_SIZE = 15;
        query.with(PageRequest.of(page-1, PAGE_SIZE));
        long count = mongoTemplate.count(query, FileMetaData.class);
        List<FileMetaData> files = mongoTemplate.find(query, FileMetaData.class);
        return PageResponse.map(metaDataMapper::toDto,
                PageResponse.<FileMetaData>builder()
                        .items(files)
                        .total(count)
                        .page(page)
                        .maxPage((int) (count/ PAGE_SIZE))
                        .build()
                );

    }

    @Override
    public void delete(String imageId) {
      template.delete(new Query(Criteria.where("_id").is(imageId)));
    }
}
