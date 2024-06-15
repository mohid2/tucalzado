package com.tucalzado.controller;


import com.tucalzado.service.IShoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final IShoeService iShoeService;

    @GetMapping(value = {"/página-principal","/"})
    public String index(Model model,@AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("shoes", iShoeService.findByBestRatingGreaterThanEqual(4));
        return "index";
    }
    @GetMapping("/sobre-nosotros")
    public String about() {
        return "otherpages/about";
    }

    @GetMapping("/contacto")
    public String contact() {
        return "otherpages/contact";
    }
}
