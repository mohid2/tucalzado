package com.tucalzado.service;



import com.tucalzado.models.dto.ShoeDTO;
import com.tucalzado.models.entity.Shoe;
import com.tucalzado.models.enums.ShoeTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface IShoeService {
    Page<Shoe> findFilteredAndPaginatedProducts(int page, String gender,String type,String brand, int pageSize);
    Shoe save(Shoe shoe, List<MultipartFile> images, MultipartFile imag, Map<String, String> formParams) throws IOException;
    Optional<Shoe> findById(Long id);
    List<Shoe> findByBestRatingGreaterThanEqual(Integer rating);
    void deleteById(Long id);
    List<Shoe> findAllByType(ShoeTypeEnum type);
    boolean validExtension(MultipartFile file) throws IOException;

    List<String> findAllBrands();

    List<ShoeDTO>  getShoeByContentName(String term);
}
