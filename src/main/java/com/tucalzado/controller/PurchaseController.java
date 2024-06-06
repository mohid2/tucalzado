package com.tucalzado.controller;

import com.tucalzado.models.dto.UserDTO;
import com.tucalzado.models.entity.Purchase;
import com.tucalzado.service.IPurchaseService;
import com.tucalzado.service.IRatingService;
import com.tucalzado.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class PurchaseController {

    private final IPurchaseService purchaseService;
    private final IUserService userService;
    private final IRatingService ratingService;


    @GetMapping("/carrito")
    public String getCart(Model model,Principal principal) {
        model.addAttribute("purchase", new Purchase());
        if(principal==null || principal.getName().equals("anonymousUser")) {
            return "redirect:/iniciar-sesion";
        }
        String username = principal.getName();
        UserDTO user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "cart";
    }
    @GetMapping("/mis-compras")
    public String getPurchase(Model model,Principal principal) {
        if(principal==null || principal.getName().equals("anonymousUser")) {
            return "redirect:/iniciar-sesion";
        }
        String username = principal.getName();
        UserDTO user = userService.getUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("purchases",purchaseService.getPurchaseByUser(user));
        return "purchases";
    }
    @PostMapping("/mis-compras")
    public ResponseEntity<?> purchase(@RequestBody Purchase purchase) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
           if (purchaseService.savePurchase(purchase, username)) {
               return ResponseEntity.ok("ok");
           }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/mis-compras/{id}")
    public String getPurchaseByUser(@PathVariable Long id, Model model) {
        Purchase purchase = purchaseService.getPurchaseById(id);
        UserDTO user = userService.getUserById(purchase.getUser().getId());

        // Asume que purchaseService.getPurchaseById(id) y userService.getUserById(id) ya están implementados
        purchase.getItems().forEach(item -> {
            boolean hasRated = ratingService.hasRated(user, item.getShoe());
            item.setHasRated(hasRated); // Supone que has agregado este campo en el item
            System.out.println(item);
        });
        model.addAttribute("purchase", purchase);
        model.addAttribute("user", user);
        return "/purchases_details";
    }

    @GetMapping("/invoice/pdf/{id}")
    public String getInvoice(@PathVariable Long id, Model model) {
        Purchase purchase = purchaseService.getPurchaseById(id);
        model.addAttribute("purchase", purchase);
        return "invoice/pdf";
    }


}