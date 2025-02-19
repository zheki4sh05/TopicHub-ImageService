package web.forum.imageservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorDto {
    private Integer code;
    private String message;
    private String localDate;
}
