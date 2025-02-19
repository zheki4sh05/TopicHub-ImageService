package web.forum.imageservice.dto;

import lombok.*;

import java.time.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImageDto {
    private String fileName;
    private LocalDate uploadDate;
    private Long imageSize;
    private String imageId;
    private String fileType;
    private byte[] file;
}
