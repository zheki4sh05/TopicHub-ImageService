package web.forum.imageservice.advice;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.multipart.*;
import web.forum.imageservice.config.i18n.*;
import web.forum.imageservice.dto.*;

import java.time.*;

@RestControllerAdvice
@AllArgsConstructor
public class ExceptionControllerAdvice {
    private final I18nUtil i18nUtil;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> exceptionEntityNotFound(MaxUploadSizeExceededException e, WebRequest request) {
        return  createResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                request);
    }


    private ResponseEntity<ErrorDto> createResponse(String errorKey, HttpStatus httpStatus, WebRequest request ){
        return new ResponseEntity<>(createErrorDto(
                i18nUtil.getMessage(errorKey, request, null),
                httpStatus
        ), httpStatus);
    }

    private ErrorDto createErrorDto(String message, HttpStatus httpStatus){
        return ErrorDto.builder()
                .code(httpStatus.value())
                .message(message)
                .localDate(LocalDate.now().toString())
                .build();
    }
}
