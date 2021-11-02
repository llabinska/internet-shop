package com.internet.shop.repository;

import com.internet.shop.dmo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory_NameAndPriceBetween(String name, Double minPrice, Double maxPrice, Pageable pageable);

    Page<Product> findByNameLikeIgnoreCaseAndPriceBetween(String name, Double minPrice, Double maxPrice, Pageable pageable);

    Page<Product> findByNameLikeIgnoreCaseAndCategory_NameAndPriceBetween(String name, String categoryName, Double minPrice, Double maxPrice, Pageable pageable);

    Page<Product> findAllByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
}
