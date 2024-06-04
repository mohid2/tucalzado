package com.tucalzado.controller;


import com.tucalzado.models.entity.Shoe;
import com.tucalzado.models.entity.ShoeStock;
import com.tucalzado.models.entity.Size;
import com.tucalzado.models.enums.Gender;
import com.tucalzado.models.enums.ShoeTypeEnum;
import com.tucalzado.repository.IShoeSizeRepository;
import com.tucalzado.service.IShoeService;
import com.tucalzado.util.paginator.PageRender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@AllArgsConstructor
@Controller
public class ShoeController {

    private final IShoeService iShoeService;
    private final IShoeSizeRepository iShoeSizeRepository;


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
        return "add_shoe_form";
    }

    @PostMapping("/agregar/calzado")
    public String saveShoe(@Valid @ModelAttribute("shoe") Shoe shoe, BindingResult result,
                           @RequestParam("images") List<MultipartFile> images,
                           @RequestParam Map<String, String> formParams,
                           @RequestParam(name = "imag") MultipartFile imag,
                           Model model) throws IOException {

        // Validar imágenes primarias
        if (imag == null || imag.isEmpty() || !iShoeService.validExtension(imag)) {
            result.rejectValue("imagePrimary", "error.shoe", "La imagen principal es inválida o no está soportada");
        }
        // Validar otras imágenes
        for (MultipartFile image : images) {
            if (image == null || image.isEmpty() || !iShoeService.validExtension(image)) {
                result.rejectValue("imageUrl", "error.shoe", "Una de las imágenes es inválida o no está soportada");
                break; // Romper el bucle después de encontrar la primera imagen inválida
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("title", "Añadir calzado");
            model.addAttribute("types", ShoeTypeEnum.values());
            List<Size> sizes = iShoeSizeRepository.findAll();
            model.addAttribute("sizes", sizes);
            model.addAttribute("genders", Gender.values());
            return "add_shoe_form";
        }
       Shoe shoeSaved = iShoeService.save(shoe, images, imag, formParams);
        return "redirect:/producto/" +shoeSaved.getId();
    }




}
