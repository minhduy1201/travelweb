package com.travelweb.service;

import com.travelweb.dto.NewDTO;

public interface INewService {
	NewDTO save(NewDTO newDTO);
//	NewDTO update(NewDTO newDTO);
	void delete(long[] ids);

}
