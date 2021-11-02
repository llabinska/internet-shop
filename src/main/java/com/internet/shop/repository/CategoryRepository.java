package com.internet.shop.repository;

import com.internet.shop.dmo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category getByName(String name);

}
