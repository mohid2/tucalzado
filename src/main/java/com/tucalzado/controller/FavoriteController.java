package com.tucalzado.controller;


import com.tucalzado.models.entity.FavoriteShoe;
import com.tucalzado.models.entity.User;
import com.tucalzado.service.IFavoriteShoeService;
import com.tucalzado.service.IUserService;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FavoriteController {

    private final IFavoriteShoeService iFavoriteProductService;
    private final IUserService userService;

    public FavoriteController(IFavoriteShoeService iFavoriteProductService, IUserService userService) {
        this.iFavoriteProductService = iFavoriteProductService;
        this.userService = userService;
    }
    @GetMapping("/favoritos")
    public String getFavorite(Model model) {
        User user =  userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        List<FavoriteShoe> favoriteProduct =  iFavoriteProductService.findAllByUserId(user.getId());
        model.addAttribute("favoriteProduct",favoriteProduct);
        return "products-favorite";
    }

    @PostMapping("/favoritos")
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteShoe favoriteProduct) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userService.getUserByUsername(username).orElseThrow();
           List<FavoriteShoe> favoriteProducts = iFavoriteProductService.findAllByUserId(user.getId());
            for (FavoriteShoe favoriteProduct1 : favoriteProducts) {
                if (favoriteProduct1.getShoe().getId() == favoriteProduct.getShoe().getId()) {
                    return ResponseEntity.status(HttpStatus.SC_CONFLICT).build();
                }
            }
            favoriteProduct.setUser(user);
            iFavoriteProductService.save(favoriteProduct);
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.badRequest().build();
    }


    @DeleteMapping("/favoritos/{id}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Long id) {
        iFavoriteProductService.delete(id);
        return ResponseEntity.ok("ok");
    }



}
