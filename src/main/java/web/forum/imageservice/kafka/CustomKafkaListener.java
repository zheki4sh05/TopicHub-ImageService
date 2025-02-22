package web.forum.imageservice.kafka;

import lombok.extern.slf4j.*;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;

@Component
@Slf4j
public class CustomKafkaListener {
    @KafkaListener(topics = "image.saved", groupId = "image")
    void listener(String data) {
        log.info("Received message [{}] in group1", data);
    }
}
