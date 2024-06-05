package com.tucalzado.models.dto;

import com.tucalzado.models.entity.FavoriteShoe;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteShoeDTO {
    private FavoriteShoeIdDTO id;
    private UserDTO user;
    private ShoeDTO shoe;
}
