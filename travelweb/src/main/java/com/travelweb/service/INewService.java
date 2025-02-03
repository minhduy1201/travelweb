package com.travelweb.service;

import com.travelweb.dto.CategoryDTO;
import com.travelweb.dto.NewDTO;
import com.travelweb.entity.CategoryEntity;
import com.travelweb.entity.NewsEntity;

import java.util.*;

import org.springframework.data.domain.Pageable;

public interface INewService {
	NewDTO save(NewDTO newDTO);
//	NewDTO update(NewDTO newDTO);
	void delete(long[] ids);
	List<NewDTO> findAll(Pageable pageable);
	int totalItem();
	List<CategoryEntity> getAllCategory();
	List<NewDTO> searchByTitle(String keyword);
//	NewsEntity findById(long id);

}
