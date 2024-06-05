package com.tucalzado.controller;

import com.tucalzado.models.dto.RatingRequestDTO;
import com.tucalzado.service.IRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.security.Principal;
import java.util.Map;
@RequiredArgsConstructor
@Controller
public class RatingController {
    private final IRatingService ratingService;

    @PostMapping("/rate-product")
    public ResponseEntity<?> rateProduct(@RequestBody RatingRequestDTO ratingRequest, Principal principal) {
        String username = principal.getName();
        Long shoeId = ratingRequest.getShoeId();
         boolean success = ratingService.saveRating(username, shoeId, ratingRequest.getRatingValue(), ratingRequest.getComment());
        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false));
        }
        return ResponseEntity.ok(Map.of("success", true));
    }
}
