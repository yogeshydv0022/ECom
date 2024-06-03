package com.work.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.exception.ResourceNotFoundException;
import com.work.model.Category;
import com.work.payload.response.CategoryDTO;
import com.work.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<CategoryDTO> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public CategoryDTO getCategoryById(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found"));
		return modelMapper.map(category, CategoryDTO.class);
	}
	
	public CategoryDTO getCategoryByNames(List<String> names) {
	List<Category> category = categoryRepository.findByNameIn(names)
				.orElseThrow(() -> new RuntimeException("Category not found"));
		return modelMapper.map(category, CategoryDTO.class);
	}
	

	public CategoryDTO addCategory(CategoryDTO categoryDTO) {
		Category category = convertToEntity(categoryDTO);
		Category savedCategory = categoryRepository.save(category);
		return convertToDto(savedCategory);
	}

//	public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
//		Category existingCategory = categoryRepository.findById(id)
//				.orElseThrow(() -> new RuntimeException("Category not found"));
//		modelMapper.map(categoryDTO, existingCategory);
//		Category updatedCategory = categoryRepository.save(existingCategory);
//		return convertToDto(updatedCategory);
//	}
	
	public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
	    Category existingCategory = categoryRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Category with ID " + id + " not found"));
	    existingCategory.setName(categoryDTO.getName());
	    Category updatedCategory = categoryRepository.save(existingCategory);
	    return convertToDto(updatedCategory);
	}


	public String deleteCategoryById(Long id) {
		Category categoryId = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " not found"));
		categoryRepository.delete(categoryId);
		return "book Deleted successfully"+id;
	
	}

	private CategoryDTO convertToDto(Category category) {
		return modelMapper.map(category, CategoryDTO.class);
	}

	private Category convertToEntity(CategoryDTO categoryDTO) {
		return modelMapper.map(categoryDTO, Category.class);
	}
}
