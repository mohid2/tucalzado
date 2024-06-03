package com.tucalzado.controller;


import com.tucalzado.service.IShoeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final IShoeService iShoeService;

    public HomeController(IShoeService iShoeService) {
        this.iShoeService = iShoeService;
    }

    @GetMapping(value = {"/página-principal","/"})
    public String index(Model model) {
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
