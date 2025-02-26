package web.forum.imageservice.dto;

import lombok.*;

import java.time.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "file")
@Setter
@Getter
public class ImageDto {
    private String id;
    private String filename;
    private LocalDate uploadDate;
    private Long imageSize;
    private String contentType;
    private String targetId;
    private String metaName;
    private byte[] file;


}
