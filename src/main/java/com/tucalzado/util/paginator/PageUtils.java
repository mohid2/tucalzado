package com.tucalzado.util.paginator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtils {
    public static <T> Page<T> createPage(List<T> content, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        List<T> contentForPage = content.subList(start, end);
        return new PageImpl<>(contentForPage, pageable, content.size());
    }
}
