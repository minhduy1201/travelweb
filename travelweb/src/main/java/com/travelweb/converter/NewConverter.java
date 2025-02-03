package com.travelweb.converter;

import org.springframework.stereotype.Component;

import com.travelweb.dto.NewDTO;
import com.travelweb.entity.NewsEntity;

@Component
public class NewConverter {
	public NewsEntity toEntity(NewDTO dto) {
		NewsEntity entity = new NewsEntity();
		entity.setTitle(dto.getTitle());
		entity.setShortDescription(dto.getShortDescription());
		entity.setTitleContent(dto.getTitleContent());
		entity.setContent(dto.getContent());
		entity.setThumbnail(dto.getThumbnail());
		entity.setCreatedBy(dto.getCreatedBy());
		return entity;
	}
	
	public NewDTO toDTO(NewsEntity entity) {
		NewDTO dto = new NewDTO();
		if(entity.getId() != null) {
			dto.setId(entity.getId());
		}
		dto.setTitle(entity.getTitle());
		dto.setShortDescription(entity.getShortDescription());
		dto.setTitleContent(entity.getTitleContent());
		dto.setContent(entity.getContent());
		dto.setThumbnail(entity.getThumbnail());
		dto.setCreatedDate(entity.getCreatedDates());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setModifiedDate(entity.getModifiedDates());
		dto.setModifiedBy(entity.getModifiedBy());
		return dto;
	}
	
	public NewsEntity toEntity(NewDTO dto, NewsEntity entity) {
		entity.setTitle(dto.getTitle());
		entity.setShortDescription(dto.getShortDescription());
		entity.setTitleContent(dto.getTitleContent());
		entity.setContent(dto.getContent());
		entity.setThumbnail(dto.getThumbnail());
		return entity;
	}
}
