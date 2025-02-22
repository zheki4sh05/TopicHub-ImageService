package web.forum.imageservice.kafka;

import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.*;

@Configuration
public class KafkaTopic {

    @Value("${spring.kafka.topic.imageSaved}")
    private String topic1Name;

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name(topic1Name).build();
    }

}
