package web.forum.imageservice.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

import java.util.*;

@Configuration
public class SwaggerUIConfig{

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.host}")
    private String hostName;

    private String createServerUrl(String hostName, String serverPort) {
        return "http://" + hostName + ":" + serverPort;
    }

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(List.of(new Server().url(createServerUrl(hostName, serverPort))))
                .info(new Info().title("Customer service API"));
    }

}