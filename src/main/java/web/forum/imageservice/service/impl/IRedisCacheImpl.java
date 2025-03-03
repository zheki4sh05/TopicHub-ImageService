package web.forum.imageservice.service.impl;

import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import web.forum.imageservice.dto.*;
import web.forum.imageservice.mapper.*;
import web.forum.imageservice.model.*;
import web.forum.imageservice.repository.*;
import web.forum.imageservice.service.*;

@Service
@AllArgsConstructor
public class IRedisCacheImpl implements IRedisCache {

    private final ImageRedisRepository imageRedisRepository;
    private final ImageRedisMutateRepository imageRedisMutateRepository;
    private final ImageMapper imageMapper;
    @Override
    public PageResponse<ImageDto> fetch(Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, 15);
        Page<ImageCache> imageCacheList = imageRedisRepository.findAll(pageRequest);
        return PageResponse.map(imageMapper::toDto, imageCacheList);
    }

    @Override
    public void cache(PageResponse<ImageDto> pageResponse) {
        imageRedisMutateRepository.saveAll(pageResponse.getItems()
                .stream()
                .map(imageMapper::fromDto).
                toList());
    }
}
