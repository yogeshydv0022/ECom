package com.work.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookId")
	private long bookId;
	
	@Column(name = "bookName")
	private String bookName;
	
	@Column(name = "Author_name")
	private String bookAuthor;
	
	@Column(name = "book_Language")
	private List<String> bookLanguage;
	
	@Column(name = "book_publisher")
	private String bookPublisher;
	
	@Column(name = "book_publishDate")
	private LocalDate bookPublisherDate=LocalDate.now();
	
	@Column(name = "book_price")
	private double bookPrice;
	
	@Column(name = "book_disPrice")
	private double bookDisPrice;
	
	@Column(name="book_stock")
	private boolean stock=true;
	
	@Column(name = "book_quantity")
	private Integer bookQuantity;
	
	@Column(name = "image_urls")
	private List<String> imagesUrl = new ArrayList<>();

	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "book_category")
	private Category category;

	

}