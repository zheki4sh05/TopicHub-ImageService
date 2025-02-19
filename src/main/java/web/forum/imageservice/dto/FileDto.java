package web.forum.imageservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FileDto {
    @NotNull
    private MultipartFile multipartFile;
}
