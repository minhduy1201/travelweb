package com.travelweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.SecondaryTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.travelweb.converter.NewConverter;
import com.travelweb.dto.NewDTO;
import com.travelweb.entity.CategoryEntity;
import com.travelweb.entity.NewsEntity;
import com.travelweb.repository.CategoryRepository;
import com.travelweb.repository.NewRepository;
import com.travelweb.service.INewService;

@Service
public class NewService implements INewService {
	
	@Autowired
	private NewRepository newRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private NewConverter newConverter;

	@Override
	public NewDTO save(NewDTO newDTO) {
		NewsEntity newEntity = new NewsEntity();
		if (newDTO.getId() != null) {
			NewsEntity oldNewEntity = newRepository.getOne(newDTO.getId());
			newEntity = newConverter.toEntity(newDTO, oldNewEntity);
		} else {
			newEntity = newConverter.toEntity(newDTO);
		}
		CategoryEntity categoryEntity = categoryRepository.findOneByCode(newDTO.getCategoryCode());
		newEntity.setCategory(categoryEntity);
		newEntity = newRepository.save(newEntity);
		return newConverter.toDTO(newEntity);
	}

	@Override
	public void delete(long[] ids) {
		for(long item: ids) {
			newRepository.deleteById(item);;
		}
		
	}

	@Override
	public List<NewDTO> findAll(Pageable pageable) {
		List<NewDTO> result = new ArrayList<>();
		List<NewsEntity> entities = newRepository.findAll(pageable).getContent();
		for (NewsEntity item : entities) {
			NewDTO newDTO = newConverter.toDTO(item);
			result.add(newDTO);
		}
		return result;
	}

	@Override
	public int totalItem() {
		return (int) newRepository.count();
	}
	
	

//	@Override
//	public NewDTO update(NewDTO newDTO) {
//		NewsEntity oldNewEntity = newRepository.getOne(newDTO.getId());
//		NewsEntity newEntity = newConverter.toEntity(newDTO, oldNewEntity);
//		CategoryEntity categoryEntity = categoryRepository.findOneByCode(newDTO.getCategoryCode());
//		newEntity.setCategory(categoryEntity);
//		newEntity = newRepository.save(newEntity);
//		return newConverter.toDTO(newEntity);
//	}
	
	
}
