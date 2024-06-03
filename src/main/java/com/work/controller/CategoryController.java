package com.work.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.work.payload.response.CategoryDTO;
import com.work.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Get all category
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Get categorybyID
 
//    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
//        CategoryDTO category = categoryService.getCategoryById(id);
//        return new ResponseEntity<>(category, HttpStatus.OK);
//    }
    
    @GetMapping("/{id}")
 	public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
 	    try {
 	        CategoryDTO category = categoryService.getCategoryById(id);
 	        return new ResponseEntity<CategoryDTO>(category, HttpStatus.OK);
 	    } catch (Exception e) {
 	        String errorMessage = "Failed to Get Category with ID: " + id + ". " + e.getMessage();
 	        return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
 	    }
 	}
    

    // Create a category
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Update  category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // Delete categorybyID
 // deleteBookById
 	@DeleteMapping("/{categoryId}")
 	public ResponseEntity<String> deteteBookById(@PathVariable Long categoryId) {
 	    try {
 	        categoryService.deleteCategoryById(categoryId);
 	        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
 	    } catch (Exception e) {
 	        String errorMessage = "Failed to delete book with ID: " + categoryId + ". " + e.getMessage();
 	        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
 	    }
 	}
}