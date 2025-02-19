package web.forum.imageservice.service;

import org.springframework.web.multipart.*;
import web.forum.imageservice.dto.*;

import java.io.*;

public interface IImageService {
    ImageDto save(MultipartFile multipartFile);

    ImageDto findById(String imageId) throws IOException;
}
