package com.tucalzado.controller;


import com.tucalzado.models.dto.AddressDTO;
import com.tucalzado.models.dto.UserDTO;
import com.tucalzado.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/iniciar-sesion")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model, Principal principal, RedirectAttributes flash) {

        if(principal!=null){
            flash.addFlashAttribute("info","Ya ha iniciado sesión antes");
            return "redirect:/";
        }
        if(error != null){
            model.addAttribute("error","Nombre de usuario o la contraseña incorrecta");
        }
        if(logout!=null){
            model.addAttribute("success","sesión cerrada");
        }
        return "user/login";
    }


    @GetMapping("/registro")
    public String getForm( Model model, RedirectAttributes flash){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            flash.addFlashAttribute("errorMessage","El usuario ya ha iniciado sesion");
            return "redirect:/tienda";
        }
        model.addAttribute("user", new UserDTO());
        return "user/register";
    }
    @PostMapping("/registro")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model )  {
        if (result.hasErrors()) {
            return "user/register";
        }
        userService.createUser(userDTO);
        return "redirect:/iniciar-sesion";
    }


    @GetMapping("/perfil")
    public String getUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            model.addAttribute("user", userService.getUserByUsername(username));
        }
        model.addAttribute("address",new AddressDTO());
        return "user/user";
    }

    @PostMapping("/perfil")
    public String updateUser(@ModelAttribute("user") UserDTO userDTO, Model model) {
        userService.updateUser(userDTO);
        return "redirect:/tienda";
    }
    @PostMapping("/perfil/direccion")
    public String updateAddressUser(@ModelAttribute("address") AddressDTO addressDTO,@RequestParam("userId") Long userId, Model model) {
        userService.updateAddressUser(addressDTO,userId);
        return "redirect:/perfil";
    }



}
