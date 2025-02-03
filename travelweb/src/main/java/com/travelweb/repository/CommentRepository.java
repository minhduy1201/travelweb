package com.travelweb.repository;

import com.travelweb.entity.CommentEntity;
import com.travelweb.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPost(NewsEntity post); // Lấy danh sách bình luận của bài viết
}

