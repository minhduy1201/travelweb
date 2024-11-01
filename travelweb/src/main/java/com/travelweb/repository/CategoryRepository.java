package com.travelweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelweb.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
	CategoryEntity findOneByCode(String code);
}
