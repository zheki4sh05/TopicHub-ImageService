package web.forum.imageservice.dto;

import lombok.*;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PageResponse<R> {
    private List<R> items;
    private Integer page;
    private Long total;
    private Integer maxPage;
    public static <T,R> PageResponse<R> map(Function<T, R> mapper, Page<T> page){
        var items = page.getContent();
        return createPageResponse(
                items.stream().map(mapper).collect(Collectors.toList()),
                page.getTotalElements(),
                page.getNumber(),
                page.getTotalPages()
        );
    }

    public static <T,R> PageResponse<R> map(Function<T, R> mapper, PageResponse<T> page){
        var items = page.getItems();
        return createPageResponse(
                items.stream().map(mapper).collect(Collectors.toList()),
                page.getTotal(),
                page.getPage(),
                page.getMaxPage()
        );
    }

    public static <T> PageResponse<T> map(Page<T> page){
        return createPageResponse(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber(),
                page.getTotalPages()
        );
    }

    private static <R> PageResponse<R> createPageResponse(List<R> items, Long total, Integer page, Integer maxPage) {
        return PageResponse.<R>builder()
                .items(items)
                .total(total)
                .page(page)
                .maxPage(maxPage)
                .build();
    }


    @Override
    public String toString() {
        return "PageResponse{" +
                "items=" + items.size() +
                ", page=" + page +
                ", total=" + total +
                ", maxPage=" + maxPage +
                '}';
    }
}
