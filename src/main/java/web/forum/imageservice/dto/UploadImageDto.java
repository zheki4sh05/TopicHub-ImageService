package web.forum.imageservice.dto;

import lombok.*;
import org.springframework.web.multipart.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadImageDto {
    private MultipartFile file;
}
