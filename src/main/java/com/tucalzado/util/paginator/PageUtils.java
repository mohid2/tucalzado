package com.tucalzado.util.paginator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtils {
    public static <T> Page<T> createPage(List<T> content, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());

        // Asegurarse de que los índices están dentro del rango válido
        if (start > end) {
            start = end;
        }

        // Asegurarse de que los índices están dentro del rango de la lista
        if (start < 0) {
            start = 0;
        }
        if (end > content.size()) {
            end = content.size();
        }

        List<T> contentForPage = content.subList(start, end);
        return new PageImpl<>(contentForPage, pageable, content.size());
    }

}
