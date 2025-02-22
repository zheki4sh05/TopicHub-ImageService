package web.forum.imageservice.dto;

import lombok.*;

import java.time.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImageDto {
    private String id;
    private String filename;
    private LocalDate uploadDate;
    private Long imageSize;
    private String contentType;
    private byte[] file;

    @Override
    public String toString() {
        return "ImageDto{" +
                "id='" + id + '\'' +
                ", filename='" + filename + '\'' +
                ", uploadDate=" + uploadDate +
                ", imageSize=" + imageSize +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
