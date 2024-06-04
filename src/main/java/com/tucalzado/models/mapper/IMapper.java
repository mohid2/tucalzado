package com.tucalzado.models.mapper;

public interface IMapper<T,K> {
     K mapToDTO(T in);

     T mapToEntity(K in);
}
