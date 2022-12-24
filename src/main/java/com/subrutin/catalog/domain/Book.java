package com.subrutin.catalog.domain;

import lombok.Data;

@Data
public class Book {
	
	private Long id;
	
	private String title;
	
	private String description;
	
	private Author author;


}