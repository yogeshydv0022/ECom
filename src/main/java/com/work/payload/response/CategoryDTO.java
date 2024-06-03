package com.work.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
   private Long id;
    private String name;
   // private List<Book> book;

    // Constructors, getters, and setters...
}