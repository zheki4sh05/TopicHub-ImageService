package web.forum.imageservice.controller;

import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.*;
import web.forum.imageservice.dto.*;
import web.forum.imageservice.exceptions.*;
import web.forum.imageservice.service.*;
import lombok.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("api/v1/image")
@AllArgsConstructor
public class ImageController {

    private final IImageService imageService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
         @NotNull @RequestPart MultipartFile multipartFile
            ){
        switch (multipartFile.getContentType()) {
            case MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_JPEG_VALUE->{
                ImageDto imageDto = imageService.save(multipartFile);
                return new ResponseEntity<>(imageDto, HttpStatus.OK);
            }
            default-> throw new UnsupportedMediaTypeStatusException();
        }
    }

    @GetMapping
    public ResponseEntity<?> fetch(
            @RequestParam String imageId
    ){
        ImageDto fileDto = null;
        try {
            fileDto = imageService.findById(imageId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getFileType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(new ByteArrayResource(fileDto.getFile()));
    }



}
