package web.forum.imageservice.exceptions;

public class UnsupportedMediaTypeStatusException extends RuntimeException{
    public UnsupportedMediaTypeStatusException(String message) {
        super(message);
    }
}
