package com.work.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.work.payload.response.BookDTO;
import com.work.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	// Add a new book by category ID
	@PostMapping("/{categoryId}")
	public ResponseEntity<BookDTO> addBookByCategoryId(@PathVariable("categoryId") Long categoryId,
			@RequestPart("bookDTO") BookDTO bookDTO, @RequestPart("imageFile") List<MultipartFile> imageFile)
			throws IOException {
		BookDTO savedBook = bookService.addBookByCategoryId(categoryId, bookDTO, imageFile);
		return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
	}

	//Does't Work
	
//	@PutMapping("/{bookId}")
//	public ResponseEntity<BookDTO> updateBookById(@PathVariable Long bookId,@RequestPart("bookDTO") BookDTO bookDTO, @RequestPart("imageFiles")List<MultipartFile> imageFiles) {
//		try {
//			BookDTO updatedBook = bookService.updateBookById(bookId, bookDTO, imageFiles);
//			return ResponseEntity.ok(updatedBook);
//		} catch (IOException e) {
//			e.printStackTrace(); // Handle the exception properly, maybe return a 500 status code
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}

	// Get all books
	@GetMapping("/all")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
		List<BookDTO> books = bookService.getAllBooks();
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	// find by all With Sorting Pagination etc
	@GetMapping("/")
	public ResponseEntity<Page<BookDTO>> searchBooks(@RequestParam(required = false) List<String> categoryNames,
			@RequestParam(required = false) String searchQuery, @RequestParam(required = false) String bookName,
			@RequestParam(required = false) List<String> bookPublishers,
			@RequestParam(required = false) List<String> bookAuthors, @RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice, @RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "bookName") String sortBy,
			@RequestParam(defaultValue = "ASC") String sortOrder) {
		Page<BookDTO> bookPage = bookService.findByBookCriteria(categoryNames, searchQuery, bookName, bookPublishers,
				bookAuthors, minPrice, maxPrice, pageNumber, pageSize, sortBy, sortOrder);
		return ResponseEntity.ok(bookPage);
	}

	 @GetMapping("/{id}")
	 	public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
	 	    try {
	 	        BookDTO category = bookService.getBookById(id);
	 	        return new ResponseEntity<BookDTO>(category, HttpStatus.OK);
	 	    } catch (Exception e) {
	 	        String errorMessage = "Failed to Get Book with ID: " + id + ". " + e.getMessage();
	 	        return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
	 	    }
	 	}

	// deleteBookById
	@DeleteMapping("/{bookId}")
	public ResponseEntity<String> deteteBookById(@PathVariable Long bookId) {
	    try {
	        bookService.deleteBookById(bookId);
	        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
	    } catch (Exception e) {
	        String errorMessage = "Failed to delete book with ID: " + bookId + ". " + e.getMessage();
	        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	    }
	}


}