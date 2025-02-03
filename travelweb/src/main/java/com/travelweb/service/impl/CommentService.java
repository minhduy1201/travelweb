package com.travelweb.service.impl;

import com.travelweb.entity.CommentEntity;
import com.travelweb.entity.NewsEntity;
import com.travelweb.entity.UserEntity;
import com.travelweb.repository.CommentRepository;
import com.travelweb.repository.NewRepository;
import com.travelweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NewRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CommentEntity> getCommentsByPostId(Long postId) {
        Optional<NewsEntity> post = newsRepository.findById(postId);
        return post.map(commentRepository::findByPost)
                   .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
    }

    public CommentEntity addComment(Long postId, Long userId, String content, int rating) {
        NewsEntity post = newsRepository.findById(postId)
                        .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        UserEntity user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setRating(rating);

        return commentRepository.save(comment);
    }
}
