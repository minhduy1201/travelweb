package com.travelweb.dto;

public class CategoryDTO extends AbstractDTO<NewDTO> {
	private String code;
	private String name;
	private String news;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNews() {
		return news;
	}
	public void setNews(String news) {
		this.news = news;
	}
	

}
