package web.forum.imageservice.service;

import web.forum.imageservice.dto.*;

public interface IRedisCache {
    PageResponse<ImageDto> fetch(Integer page);

    void cache(PageResponse<ImageDto> pageResponse);
}
