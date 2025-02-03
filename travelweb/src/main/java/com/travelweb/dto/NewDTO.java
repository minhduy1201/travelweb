package com.travelweb.dto;

import java.util.List;

import com.travelweb.entity.NewsEntity;

public class NewDTO extends AbstractDTO<NewDTO> {
	
	private String title;
	private String shortDescription;
	private String titleContent;
	private String content;
	private String categoryCode;
	private String thumbnail;
	private List<CommentDTO> comments;
	
	
	
	public NewDTO() {
		super();
	}

	public NewDTO(NewsEntity post) {
		// TODO Auto-generated constructor stub
	}

	public NewDTO(String post) {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String getTitleContent() {
	    return titleContent;
	}

	public void setTitleContent(String titleContent) {
	    this.titleContent = titleContent;
	}

	public List<CommentDTO> getComments() {
		return comments;
	}

	public void setComments(List<CommentDTO> comments) {
		this.comments = comments;
	}
}
