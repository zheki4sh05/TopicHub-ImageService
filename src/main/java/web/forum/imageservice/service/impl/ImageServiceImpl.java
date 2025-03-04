package web.forum.imageservice.service.impl;

import com.mongodb.*;
import com.mongodb.client.gridfs.model.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.apache.commons.io.*;
import org.bson.types.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.gridfs.*;
import org.springframework.kafka.core.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;
import web.forum.imageservice.dto.*;
import web.forum.imageservice.mapper.*;
import web.forum.imageservice.model.*;
import web.forum.imageservice.service.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService {

    @Value("${spring.kafka.topic.imageSaved}")
    private String topic1Name;


    private final GridFsTemplate template;
    private final GridFsOperations operations;
    private final MongoTemplate mongoTemplate;
    private final MetaDataMapper metaDataMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final IRedisCache imageMetaDataCache;

    @Override
    public ImageDto save(MultipartFile multipartFile,String targetId,String name) {
        DBObject metadata = new BasicDBObject();
        metadata.put("targetId", targetId);
        metadata.put("imageName", name);
        try {
            String fileName = multipartFile.getOriginalFilename();
            ObjectId fileID = template.store(
                    multipartFile.getInputStream(),
                    fileName,
                    multipartFile.getContentType(),
                    metadata);

            var imageDto =  ImageDto.builder()
                    .filename(fileName)
                    .imageSize(multipartFile.getSize())
                    .uploadDate(LocalDate.now())
                    .id(fileID.toString())
                    .build();

          var kafkaRequest = kafkaTemplate.send(topic1Name, fileID.toString());
            kafkaRequest.whenComplete((result, exception)->{
                if(exception!=null){
                        log.error("failed kafka send message {}", exception.getMessage());
                }else{
                    log.info("message sent successfully:{}", result.getRecordMetadata());
                }
            });
            return imageDto;
        } catch (IOException e) {
            log.error("file image upload {}", e.getMessage());
            throw new RuntimeException(e);
        }



    }

    @Override
    public ImageDto findById(String imageId) throws IOException {
        GridFSFile gridFSFile = template.findOne( new Query(Criteria.where("_id").is(imageId)) );

        byte[] image;
        ImageDto loadFile = new ImageDto();
        if(gridFSFile==null){
//            throw new EntityNotFoundException();
            try(InputStream inputStream =  ImageServiceImpl.class.getResourceAsStream("/default.png")){
                image = inputStream.readAllBytes();
                loadFile.setContentType("image/png");
                loadFile.setTargetId("");
                loadFile.setFilename("default");

            }
        }else{
            image  = IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream());
            if ( gridFSFile.getMetadata() != null) {
                loadFile.setFilename( gridFSFile.getFilename() );
                loadFile.setContentType( gridFSFile.getMetadata().get("_contentType").toString() );
                loadFile.setTargetId((String) gridFSFile.getMetadata().get("targetId"));

            }
                  }
        loadFile.setFile(image);
        return loadFile;
    }

    @Override
//    @Cacheable(value = "imagePage", key = "#page")
    public PageResponse<ImageDto> fetch(Integer page) {

//        PageResponse<ImageDto> pageResponse = imageMetaDataCache.fetch(page);
//        if(pageResponse.getItems().size()==0){
//            pageResponse = fileMetaDataList(page);
//            imageMetaDataCache.cache(pageResponse);
//        }
//        return pageResponse;
        Query query = new Query(Criteria.where("metadata.imageName").ne("logo"));
        final int PAGE_SIZE = 15;
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

    private PageResponse<ImageDto> fileMetaDataList(Integer page){
        Query query = new Query();
        final int PAGE_SIZE = 15;
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
//    @CacheEvict(value = "imageById", key = "#imageId")
    public void delete(String imageId) {
      template.delete(new Query(Criteria.where("_id").is(imageId)));
    }

    @Override
//    @Cacheable(value = "imageSearch", key = "#name + '-' + #page")
    public PageResponse<ImageDto> search(String name,Integer page) {
        Query query = new Query(Criteria.where("metadata.imageName").regex(".*" + name + ".*").andOperator(Criteria.where("metadata.imageName").ne("logo")));
        long count = mongoTemplate.count(query, FileMetaData.class);
        List<FileMetaData> files = mongoTemplate.find(query, FileMetaData.class);
        return PageResponse.<ImageDto>builder()
                .total(count)
                .items(files.stream().map(metaDataMapper::toDto).collect(Collectors.toList()))
                .page(1)
                .build();
    }

    @Override
//    @Cacheable(value = "imageMeta", key = "#id")
    public ImageDto getById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        FileMetaData fileMetaData = mongoTemplate.findOne(query, FileMetaData.class);
        return metaDataMapper.toDto(fileMetaData);
    }
}
