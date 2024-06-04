package com.tucalzado.service.impl;


import com.tucalzado.models.entity.ImageUrl;
import com.tucalzado.models.entity.Shoe;
import com.tucalzado.models.entity.ShoeStock;
import com.tucalzado.models.entity.ShoeStockId;
import com.tucalzado.models.enums.ShoeTypeEnum;
import com.tucalzado.repository.IShoeRepository;
import com.tucalzado.repository.IShoeSizeRepository;
import com.tucalzado.repository.IShoeStockRepository;
import com.tucalzado.service.IShoeService;
import com.tucalzado.service.IUploadFileService;
import com.tucalzado.util.paginator.PageUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ShoeServiceImpl implements IShoeService {
    private final IShoeRepository iShoeRepository;
    private final IShoeSizeRepository iShoeSizeRepository;
    private final IShoeStockRepository shoeStockRepository;
    private final IUploadFileService iUploadFileService;
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    @Override
    @Transactional(readOnly = true)
    public List<Shoe> findAll() {
        return iShoeRepository.findAll();
    }

    @Override
    public Page<Shoe> findFilteredAndPaginatedProducts(int page,String gender,String type, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        List<Shoe> filteredProducts = iShoeRepository.findAll();

        if (gender != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(product -> product.getGender().getGenderEnum().equalsIgnoreCase(gender))
                    .collect(Collectors.toList());
        }
        if (type != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(product -> product.getType().getTypeEnum().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }
        return PageUtils.createPage(filteredProducts, pageRequest);
    }

    @Override
    @Transactional
    public Shoe save(Shoe shoe, List<MultipartFile> images, MultipartFile imag, Map<String, String> formParams) throws IOException {
        // Crear un mapa para almacenar los ID de talla y sus respectivos stocks
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

        // Imprimir el mapa de tallas y stocks
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
        return savedShoe;
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
}
