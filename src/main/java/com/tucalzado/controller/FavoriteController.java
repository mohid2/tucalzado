package com.tucalzado.controller;


import com.tucalzado.models.dto.FavoriteShoeDTO;
import com.tucalzado.models.dto.UserDTO;

import com.tucalzado.models.entity.FavoriteShoeId;
import com.tucalzado.service.IFavoriteShoeService;
import com.tucalzado.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@RequiredArgsConstructor
@Controller
public class FavoriteController {

    private final IFavoriteShoeService iFavoriteProductService;
    private final IUserService userService;


    @GetMapping("/favoritos")
    public String getFavorite(Model model,Principal principal) {
        if(principal==null || principal.getName().equals("anonymousUser")) {
            return "redirect:/iniciar-sesion";
        }
        UserDTO user =  userService.getUserByUsername(principal.getName());
        model.addAttribute("favoriteProduct",iFavoriteProductService.findAllByUserId(user.getId()));
        return "products-favorite";
    }

    @PostMapping("/favoritos")
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteShoeDTO favoriteShoeDTO, Principal principal) {
        if (principal != null && !principal.equals("anonymousUser")) {
            String username = principal.getName();
            boolean save =  iFavoriteProductService.save(favoriteShoeDTO,username);
            if (!save) {
                return ResponseEntity.status(HttpStatus.SC_CONFLICT).build();
            }
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/favoritos/borrar")
    public ResponseEntity<?> deleteFavorite(@RequestBody FavoriteShoeId id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        iFavoriteProductService.delete(id);
        return ResponseEntity.ok("ok");
    }



}
