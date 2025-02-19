package web.forum.imageservice.model;

import com.mongodb.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.*;

@Document(collection = "fs.files")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileMetaData {
    @Id
    private String id;
    private String filename;
    private DBObject metadata;
    private LocalDate uploadDate;
    private Long chunkSize;
}
