package com.travelweb.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	 @JsonBackReference // Ngăn chặn vòng lặp JSON
	private NewsEntity post; // Liên kết với bảng bài viết

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user; // Liên kết với bảng người dùng

	@Column(name = "content", nullable = false, length = 1000)
	private String content; // Nội dung bình luận

	@Column(name = "rating", nullable = false)
	private int rating; // Số sao đánh giá (1-5)

	@Column(name = "created_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate; // Thời gian tạo bình luận

	public CommentEntity() {
		this.createdDate = new Date();
	}

	public NewsEntity getPost() {
		return post;
	}

	public void setPost(NewsEntity post) {
		this.post = post;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
