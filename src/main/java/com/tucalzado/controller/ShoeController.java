package com.tucalzado.controller;


import com.tucalzado.entity.*;
import com.tucalzado.entity.enums.Gender;
import com.tucalzado.entity.enums.ShoeTypeEnum;
import com.tucalzado.repository.IShoeSizeRepository;
import com.tucalzado.repository.IShoeStockRepository;
import com.tucalzado.service.IShoeService;
import com.tucalzado.service.IUploadFileService;
import com.tucalzado.util.paginator.PageRender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;



@Controller
public class ShoeController {

    private final IShoeService iShoeService;
    private final IUploadFileService iUploadFileService;
    private final IShoeSizeRepository iShoeSizeRepository;
    private final IShoeStockRepository shoeStockRepository;

    public ShoeController(IShoeService iShoeService, IUploadFileService iUploadFileService, IShoeSizeRepository iShoeSizeRepository, IShoeStockRepository shoeStockRepository) {
        this.iShoeService = iShoeService;
        this.iUploadFileService = iUploadFileService;
        this.iShoeSizeRepository = iShoeSizeRepository;
        this.shoeStockRepository = shoeStockRepository;
    }

    @GetMapping("/tienda")
    public String getShop(HttpServletRequest request, @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "gender", required = false) String gender,
                          @RequestParam(name = "type", required = false) String type,
                          Model model) {
        String userAgent = request.getHeader("User-Agent");
        int itemsPerPage = 9; // Número predeterminado de elementos por página

        // Verificar si el User-Agent indica que es un dispositivo móvil
        if (userAgent != null && userAgent.toLowerCase().contains("mobile")) {
            itemsPerPage = 10; // Ajustar el número de elementos por página para dispositivos móviles
        }

        Page<Shoe> productListPage = iShoeService.findFilteredAndPaginatedProducts(page,gender,type, itemsPerPage);
        String[] queryParams = buildQueryParams(type,gender);
        PageRender<Shoe> pageRender = new PageRender<>("/tienda", productListPage, queryParams);
        model.addAttribute("shoes", productListPage.getContent());
        model.addAttribute("page", pageRender);
        model.addAttribute("title", "Tienda");
        return "shop";
    }
    private String[] buildQueryParams(String type, String gender) {
        List<String> queryParams = new ArrayList<>();
        if (type != null && !type.isEmpty()) {
            queryParams.add("type=" + type);
        }
        if (gender != null && !gender.isEmpty()) {
            queryParams.add("gender=" + gender);
        }
        return queryParams.isEmpty() ? null : queryParams.toArray(new String[0]);
    }

    @GetMapping("/producto/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Optional<Shoe> shoeOptional = iShoeService.findById(id);
        if (shoeOptional.isEmpty()) {
            return "redirect:/tienda";
        }
        Shoe shoe = shoeOptional.get();
        // Filtrar las ShoeStock para excluir aquellas con stock 0
        List<ShoeStock> filteredShoeStocks = shoe.getShoeStocks().stream()
                .filter(shoeStock -> shoeStock.getStock() > 0)
                .toList();
        // Establecer la lista filtrada en el objeto Shoe
        shoe.setShoeStocks(filteredShoeStocks);
        // Dividir la descripción en líneas
        List<String> descriptionLines = Arrays.asList(shoe.getDescription().split("\n"));
        model.addAttribute("shoe", shoe);
        model.addAttribute("descriptionLines", descriptionLines);
        model.addAttribute("shoes", iShoeService.findAllByType(shoe.getType()));
        return "shop-single";
    }

    @GetMapping("/agregar/calzado")
    public String showAddShoeForm(Model model) {
        model.addAttribute("title", "Añadir calzado");
        model.addAttribute("types", ShoeTypeEnum.values());
        List<Size> sizes = iShoeSizeRepository.findAll();
        model.addAttribute("sizes", sizes);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("shoe", new Shoe());
        return "products/add_shoe_form";
    }

    @PostMapping("/agregar/calzado")
    public String saveShoe(@Valid @ModelAttribute("shoe") Shoe shoe,
                           @RequestParam("images") List<MultipartFile> images,
                           @RequestParam Map<String, String> formParams,
                           @RequestParam(name = "imag") MultipartFile imag,
                           Model model) throws IOException {
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
                try {
                    // Save each image to the server
                    imageUrls.add(iUploadFileService.save(image));
                } catch (IOException e) {
                    // Handle exception if image saving fails
                    e.printStackTrace();
                    // You can choose to throw an exception or handle it gracefully
                }
            }
            // Set the image URLs in the product
            shoe.setImagePrimary(iUploadFileService.save(imag).getUrl());
            shoe.setImageUrl(imageUrls);
        }

        // Guardar el zapato primero
        Shoe savedShoe = iShoeService.save(shoe);

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
        return "redirect:/producto/" + savedShoe.getId();
    }

}
