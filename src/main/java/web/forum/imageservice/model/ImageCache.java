package web.forum.imageservice.model;

import lombok.*;
import org.springframework.data.redis.core.*;

import java.time.*;

@RedisHash("image")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ImageCache{
    private String id;
    private String filename;
    private LocalDate uploadDate;
    private Long imageSize;
    private String contentType;
    private String targetId;
    private String metaName;
}