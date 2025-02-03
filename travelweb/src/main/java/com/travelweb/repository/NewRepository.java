package com.travelweb.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.travelweb.entity.NewsEntity;

public interface NewRepository extends JpaRepository<NewsEntity, Long>{
//	NewsEntity findOne(Long id);
	@Query("SELECT n FROM NewsEntity n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<NewsEntity> findByTitleContaining(@Param("keyword") String keyword);

	Collection<String> findByCreatedBy(Long userId);
	
}
