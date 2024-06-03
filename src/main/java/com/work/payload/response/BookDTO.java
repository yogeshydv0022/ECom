package com.work.payload.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
	private long bookId;
    private String bookName;
    private String bookAuthor;
    private List<String> bookLanguage;
    private String bookPublisher;
    private LocalDate bookPublisherDate =LocalDate.now();
    private boolean stock=true;
    private double bookPrice;
    private double bookDisPrice;
    private Integer bookQuantity;
    private List<String>bookImages;
    private CategoryDTO categoryId; 
    
    
    
}
