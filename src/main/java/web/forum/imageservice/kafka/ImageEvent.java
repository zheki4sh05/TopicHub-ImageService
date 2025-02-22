package web.forum.imageservice.kafka;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ImageEvent {
    private String imageId;
}
