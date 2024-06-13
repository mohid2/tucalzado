package com.tucalzado.service.impl;


import com.tucalzado.models.dto.ShoeDTO;
import com.tucalzado.models.entity.ImageUrl;
import com.tucalzado.models.entity.Shoe;
import com.tucalzado.models.entity.ShoeStock;
import com.tucalzado.models.entity.ShoeStockId;
import com.tucalzado.models.enums.ShoeTypeEnum;
import com.tucalzado.models.mapper.IShoeMapper;
import com.tucalzado.repository.*;
import com.tucalzado.service.IShoeService;
import com.tucalzado.service.IUploadFileService;
import com.tucalzado.util.paginator.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShoeServiceImpl implements IShoeService {
    private final IShoeRepository iShoeRepository;
    private final IShoeSizeRepository iShoeSizeRepository;
    private final IShoeStockRepository shoeStockRepository;
    private final IUploadFileService iUploadFileService;
    private final IItemRepository iItemRepository;
    private final IFavoriteShoeRepository iFavoriteShoeRepository;
    private final IShoeMapper iShoeMapper;
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    @Override
    public Page<Shoe> findFilteredAndPaginatedProducts(int page,String gender,String type,String brand, int pageSize) {
        List<Shoe> filteredProducts = iShoeRepository.findAll();

        if (gender != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(product -> normalize(product.getGender().getGenderEnum()).equalsIgnoreCase(normalize(gender)))
                    .collect(Collectors.toList());
        }

        if (type != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(product -> normalize(product.getType().getTypeEnum()).equalsIgnoreCase(normalize(type)))
                    .collect(Collectors.toList());
        }

        if (brand != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(product -> normalize(product.getBrand()).equalsIgnoreCase(normalize(brand)))
                    .collect(Collectors.toList());
        }

        Pageable pageRequest = PageRequest.of(page, pageSize);
        return PageUtils.createPage(filteredProducts, pageRequest);
    }

    private String normalize(String input) {
        if (input == null) {
            return null;
        }
        // Normaliza la cadena para eliminar acentos y otros caracteres diacríticos
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Elimina los caracteres diacríticos
        normalized = normalized.replaceAll("\\p{M}", "");
        // Convierte a minúsculas y elimina espacios adicionales
        return normalized.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    @Override
    @Transactional
    public Shoe save(Shoe shoe, List<MultipartFile> images, MultipartFile imag, Map<String, String> formParams) throws IOException {
        Map<Long, Integer> sizeStockMap = getSizeStockMap(formParams);

        // Check if images are uploaded
        if (imag != null && !imag.isEmpty() || images != null && !images.isEmpty()) {
            List<ImageUrl> imageUrls = new ArrayList<>();
            // Process each image
            for (MultipartFile image : images) {
                // Save each image to the server
                imageUrls.add(iUploadFileService.save(image));
            }
            // Set the image URLs in the product
            shoe.setImagePrimary(iUploadFileService.save(imag).getUrl());
            shoe.setImageUrl(imageUrls);
        }
        // Guardar el zapato primero
        Shoe savedShoe = iShoeRepository.save(shoe);
        ShoeStockSave(sizeStockMap, savedShoe);
        return savedShoe;
    }

    private void ShoeStockSave(Map<Long, Integer> sizeStockMap, Shoe savedShoe) {
        for (Map.Entry<Long, Integer> entry : sizeStockMap.entrySet()) {
            Long sizeId = entry.getKey();
            Integer stock = entry.getValue();
            // Crear una nueva instancia de ShoeStock
            ShoeStock shoeStock = new ShoeStock();
            shoeStock.setId(new ShoeStockId(savedShoe.getId(), sizeId));
            shoeStock.setShoe(savedShoe);
            shoeStock.setSize(iShoeSizeRepository.findById(sizeId).orElseThrow(() -> new RuntimeException("Size not found")));
            shoeStock.setStock(stock);
            // Guardar ShoeStock
            shoeStockRepository.save(shoeStock);
        }
    }
    private  Map<Long, Integer> getSizeStockMap(Map<String, String> formParams) {
        Map<Long, Integer> sizeStockMap = new HashMap<>();
        // Iterar sobre los parámetros del formulario para obtener los datos del stock
        for (Map.Entry<String, String> entry : formParams.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if (paramName.startsWith("stock-")) {
                Long sizeId = Long.parseLong(paramName.substring("stock-".length()));
                Integer stock = paramValue.isEmpty() ? 0 : Integer.parseInt(paramValue); // Convertir cadena vacía a 0
                sizeStockMap.put(sizeId, stock);
            }
        }
        return sizeStockMap;
    }

    public boolean validExtension(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        // Extract file extension
        String fileExtension = getFileExtension(fileName);
        if (!SUPPORTED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            return false;
        }

        // Check if the file is a valid image
        BufferedImage image = ImageIO.read(file.getInputStream());
        return image != null;
    }

    @Override
    public List<String> findAllBrands() {
        return iShoeRepository.findAllBrand();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        Shoe shoe = iShoeRepository.findById(id).orElseThrow();
        for (ImageUrl image : shoe.getImageUrl()) {
            iUploadFileService.delete(image.getUrl());
        }
        iUploadFileService.delete(shoe.getImagePrimary());
        // Eliminar todas las referencias en favorite_products
        iFavoriteShoeRepository.deleteByShoeId(shoe.getId());
        iFavoriteShoeRepository.deleteByShoeId(shoe.getId());
        iItemRepository.deleteByShoeId(shoe.getId());
        iShoeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shoe> findAllByType(ShoeTypeEnum type) {
        return iShoeRepository.findAllByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shoe> findById(Long id) {
        return iShoeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shoe> findByBestRatingGreaterThanEqual(Integer rating) {
        return iShoeRepository.findByRatingGreaterThanEqual(rating);
    }

    @Override
    public List<ShoeDTO> getShoeByContentName(String term) {
        return iShoeRepository.findShoeByNameLikeIgnoreCase(term).stream().map(iShoeMapper::shoeToShoeDTO).toList();
    }
}
