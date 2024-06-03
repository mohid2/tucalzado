package com.tucalzado.repository;

import com.tucalzado.entity.Size;
import com.tucalzado.entity.enums.ShoeSizeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IShoeSizeRepository extends JpaRepository<Size, Long> {
    List<Size> findAllByShoeSizeIn(List<ShoeSizeEnum> shoeSizes);

}
