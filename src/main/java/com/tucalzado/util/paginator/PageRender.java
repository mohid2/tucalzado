package com.tucalzado.util.paginator;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
@Getter
public class PageRender<T> {
    private final String url;
    private final Page<T> page;
    private final int totalPages;
    private final int numberOfItemsByPage;
    private final int actualPage;
    private String[] queryParams; // Array para almacenar los parámetros de filtro
    private final List<PageItem> pages = new ArrayList<>(); // Inicialización directa al declarar

    public PageRender(String url, Page<T> page, String... queryParams) {
        this.url = url;
        this.page = page;
        if (queryParams != null && queryParams.length > 0) {
            this.queryParams = queryParams;// Almacenar los parámetros de filtro
        }
        numberOfItemsByPage = page.getSize();
        totalPages = page.getTotalPages();
        actualPage = page.getNumber() + 1;

        int desde, hasta;
        if (totalPages <= numberOfItemsByPage) {
            desde = 1;
            hasta = totalPages;
        } else {
            if (actualPage <= numberOfItemsByPage / 2) {
                desde = 1;
                hasta = numberOfItemsByPage;
            } else if (actualPage >= totalPages - numberOfItemsByPage / 2) {
                desde = totalPages - numberOfItemsByPage + 1;
                hasta = numberOfItemsByPage;
            } else {
                desde = actualPage - numberOfItemsByPage / 2;
                hasta = numberOfItemsByPage;
            }
        }
        for (int i = 0; i < hasta; i++) {
            pages.add(new PageItem(desde + i, actualPage == desde + i));
        }
    }

    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public boolean isHasNext() {
        return page.hasNext();
    }

    public boolean isHasPrevius() {
        return page.hasPrevious();
    }

    // Método para generar la URL con los parámetros de filtro
    public String getUrlWithParams(int page) {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?page=").append(page); // Añadir número de página

        // Añadir los parámetros de filtro si existen
        if (queryParams != null && queryParams.length > 0) {
            for (String param : queryParams) {
                builder.append("&").append(param);
            }
        }
        return builder.toString();
    }
}
