package web.forum.imageservice.repository;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;
import web.forum.imageservice.model.*;

@Repository
public interface ImageRedisRepository extends PagingAndSortingRepository<ImageCache, String> {

}
