package com.travelweb.converter;

import com.travelweb.dto.CategoryDTO;
import com.travelweb.entity.CategoryEntity;

public class CategoryConverter {
	public CategoryEntity toEntity(CategoryDTO dto) {
		CategoryEntity entity = new CategoryEntity();
		entity.setCode(dto.getCode());
		entity.setName(dto.getName());
		return entity;
	}
	
	public CategoryDTO toDTO(CategoryEntity entity) {
		CategoryDTO dto = new CategoryDTO();
//		if(entity.getId() != null) {
//			dto.setId(entity.getId());
//		}
		dto.setCode(entity.getCode());
		dto.setName(entity.getName());
		return dto;
	}
	
	public CategoryEntity toEntity(CategoryDTO dto, CategoryEntity entity) {
		entity.setCode(dto.getCode());
		entity.setName(dto.getName());
		return entity;
	}
}
