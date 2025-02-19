package web.forum.imageservice.dto;

public enum ErrorKey {
    UNSUPPORTED("error.unsupported"),
    SIZE("error.file_size");
    private final String key;

    ErrorKey(String type) {
        this.key = type;
    }

    public String key() {
        return key;
    }
}
