package web.forum.imageservice.controller;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import web.forum.imageservice.dto.*;
import web.forum.imageservice.exceptions.*;
import web.forum.imageservice.service.*;

import java.io.*;

@RestController
@RequestMapping("api/v1/image")
@AllArgsConstructor
public class ImageController {

    private final IImageService imageService;

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(
            @RequestPart("file") MultipartFile file,
            @RequestPart("targetId") String targetId,
            @RequestPart("name") String name
    ) {

        switch (file.getContentType()) {
            case MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE -> {
                ImageDto imageDto = imageService.save(file, targetId, name);
                return new ResponseEntity<>(imageDto, HttpStatus.OK);
            }
            default -> throw new UnsupportedMediaTypeStatusException(ErrorKey.UNSUPPORTED.key());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> fetch(
            @RequestParam("imageId") String imageId
    ) {
        ImageDto fileDto;
        try {
            fileDto = imageService.findById(imageId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFilename() + "\"")
                .body(new ByteArrayResource(fileDto.getFile()));
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponse<ImageDto>> fetchList(
            @NotNull @RequestParam("page") Integer page
    ) {
        return new ResponseEntity<>(imageService.fetch(page), HttpStatus.OK);
    }



    @DeleteMapping("")
    public ResponseEntity<String> delete(
            @RequestParam("imageId") String imageId
    ) {
        imageService.delete(imageId);
        return new ResponseEntity<>(imageId, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ImageDto>> search(
            @RequestParam("name") String name,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        PageResponse<ImageDto> pageResponse = imageService.search(name, page);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<ImageDto> findById(
            @RequestParam("imageId") String id
    ) {
        ImageDto pageResponse = imageService.getById(id);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }
}
