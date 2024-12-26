package com.travelweb.service;

import com.travelweb.dto.NewDTO;

import java.util.*;

import org.springframework.data.domain.Pageable;

public interface INewService {
	NewDTO save(NewDTO newDTO);
//	NewDTO update(NewDTO newDTO);
	void delete(long[] ids);
	List<NewDTO> findAll(Pageable pageable);
	int totalItem();

}
