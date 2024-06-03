package com.tucalzado.service.impl;


import com.tucalzado.entity.Shoe;
import com.tucalzado.entity.enums.ShoeTypeEnum;
import com.tucalzado.repository.IShoeRepository;
import com.tucalzado.service.IShoeService;
import com.tucalzado.util.paginator.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ShoeServiceImpl implements IShoeService {
    private final IShoeRepository productRepository;

    public ShoeServiceImpl(IShoeRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Shoe> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Shoe> findFilteredAndPaginatedProducts(int page,String gender,String type, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        List<Shoe> filteredProducts = productRepository.findAll();

        if (gender != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(product -> product.getGender().getGenderEnum().equalsIgnoreCase(gender))
                    .collect(Collectors.toList());
        }
        if (type != null) {
            filteredProducts = filteredProducts.stream()
                    .filter(product -> product.getType().getTypeEnum().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }
        return PageUtils.createPage(filteredProducts, pageRequest);
    }

    @Override
    @Transactional
    public Shoe save(Shoe shoe) {
        return productRepository.save(shoe);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Shoe> findAllByType(ShoeTypeEnum type) {
        return productRepository.findAllByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shoe> findById(Long id) {
        return productRepository.findById(id);
    }



    @Override
    public List<Shoe> findByBestRatingGreaterThanEqual(Integer rating) {
        return productRepository.findByRatingGreaterThanEqual(rating);
    }
}
