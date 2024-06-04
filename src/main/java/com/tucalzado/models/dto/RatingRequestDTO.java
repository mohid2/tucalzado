package com.tucalzado.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequestDTO {
    private Long shoeId;
    private int ratingValue;
    private String comment;
}
