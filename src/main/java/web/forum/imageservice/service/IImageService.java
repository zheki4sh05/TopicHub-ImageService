package web.forum.imageservice.service;

import org.springframework.web.multipart.*;
import web.forum.imageservice.dto.*;

import java.io.*;

public interface IImageService {
    ImageDto save(MultipartFile multipartFile,String targetId,String name);

    ImageDto findById(String imageId) throws IOException;

    PageResponse<ImageDto> fetch(Integer page);

    void delete(String imageId);

    PageResponse<ImageDto> search(String name,Integer page);

    ImageDto getById(String id);
}
