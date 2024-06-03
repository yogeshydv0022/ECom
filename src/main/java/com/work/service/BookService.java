package com.work.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.work.exception.FileNotSupportedException;
import com.work.exception.ResourceNotFoundException;
import com.work.model.Book;
import com.work.model.Category;
import com.work.payload.response.BookDTO;
import com.work.repository.BookRepository;
import com.work.repository.CategoryRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CategoryRepository categoryRepository;

	public BookService() throws IOException {

	}

	private static final Path UPLOAD_PATH;

	static {
		try {
			UPLOAD_PATH = Paths.get(new ClassPathResource("").getFile().getAbsolutePath() + File.separator + "static"
					+ File.separator + "image");
			if (!Files.exists(UPLOAD_PATH)) {
				Files.createDirectories(UPLOAD_PATH);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error initializing UPLOAD_PATH", e);
		}
	}

	public BookDTO addBookByCategoryId(Long categoryId, BookDTO bookDTO, List<MultipartFile> imageFiles)
			throws IOException {
		// Find the category by its ID
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		// convert BookDTO to Book entity
		Book book = convertToEntity(bookDTO);

		// Set the book's category
		book.setCategory(category);

		// Save book images
		List<String> imageUris = new ArrayList<>();
		for (MultipartFile file : imageFiles) {
			if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")
					&& !file.getContentType().equals("image/gif") && !file.getContentType().equals("image/bmp")) {
				throw new FileNotSupportedException("Only .jpeg, .png, .gif, and .bmp images are supported");
			}
			String timeStampedFileName = new SimpleDateFormat("ssmmHHddMMyyyy").format(new Date()) + "_"
					+ file.getOriginalFilename();
			Path filePath = UPLOAD_PATH.resolve(timeStampedFileName);
			Files.copy(file.getInputStream(), filePath);
			String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/")
					.path(timeStampedFileName).toUriString();
			imageUris.add(fileUri);
		}
		book.setImagesUrl(imageUris);

		// Save the book to the data store
		bookRepository.save(book);

		// Map Book entity back to BookDTO and return
		return modelMapper.map(book, BookDTO.class);
	}

	// getAll Books
	public List<BookDTO> getAllBooks() {
		List<Book> books = bookRepository.findAll();
		return books.stream().map(book -> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
	}

	// findByID
	public BookDTO getBookById(Long id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
		return modelMapper.map(book, BookDTO.class);
	}

	// Update By ID //Does't Work
//	public BookDTO updateBookById(Long bookId, BookDTO bookDTO, List<MultipartFile> imageFiles) throws IOException {
//	    // Find the book by its ID
//	    Book book = bookRepository.findById(bookId)
//	            .orElseThrow(() -> new RuntimeException("Book not found"));
//
//	    // Update book fields from DTO
//	    book.setBookName(bookDTO.getBookName());
//	    book.setBookAuthor(bookDTO.getBookAuthor());
//	    book.setBookLanguage(bookDTO.getBookLanguage());
//	    book.setBookPublisher(bookDTO.getBookPublisher());
//	   // book.setBookPublisherDate(bookDTO.getBookPublisherDate());
//	    book.setStock(bookDTO.isStock());
//	    book.setBookPrice(bookDTO.getBookPrice());
//	    book.setBookDisPrice(bookDTO.getBookDisPrice());
//	    book.setBookQuantity(bookDTO.getBookQuantity());
//
//	    Category foundCategory = categoryRepository.findById(bookDTO.getCategoryId().getId())
//	            .orElseThrow(() -> new RuntimeException("Category not found"));
//	    book.setCategory(foundCategory);
//
//	    // Save book images (if provided)
//	    if (imageFiles != null && !imageFiles.isEmpty()) {
//	        List<String> imageUris = new ArrayList<>();
//	        for (MultipartFile file : imageFiles) {
//	            if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")
//	                    && !file.getContentType().equals("image/gif") && !file.getContentType().equals("image/bmp")) {
//	                throw new FileNotSupportedException("Only .jpeg, .png, .gif, and .bmp images are supported");
//	            }
//	            String timeStampedFileName = new SimpleDateFormat("ssmmHHddMMyyyy").format(new Date()) + "_"
//	                    + file.getOriginalFilename();
//	            Path filePath = UPLOAD_PATH.resolve(timeStampedFileName);
//	            Files.copy(file.getInputStream(), filePath);
//	            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/")
//	                    .path(timeStampedFileName).toUriString();
//	            imageUris.add(fileUri);
//	        }
//	        book.setImagesUrl(imageUris);
//	    }
//
//	    // Save the updated book to the data store
//	    bookRepository.save(book);
//
//	    // Map Book entity back to BookDTO and return
//	    return modelMapper.map(book, BookDTO.class);
//	}


	// Delete By ID
	public void deleteBookById(Long id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
		bookRepository.deleteById(book.getBookId());
	}

	// AllByFilterAndSortAndPagination
	public Page<BookDTO> findByBookCriteria(List<String> categoryNames, String searchQuery, String bookName,
			List<String> bookPublishers, List<String> bookAuthors, Double minPrice, Double maxPrice, int pageNumber,
			int pageSize, String sortBy, String sortOrder) {

		Objects.requireNonNull(pageNumber, "Page number cannot be null");
		Objects.requireNonNull(pageSize, "Page size cannot be null");

		if (pageNumber < 0 || pageSize <= 0) {
			throw new IllegalArgumentException("Invalid page number or size");
		}

		Specification<Book> spec = Specification.where(null);

		if (searchQuery != null && !searchQuery.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.or(
					builder.like(root.get("category").get("name"), "%" + searchQuery + "%"),
					builder.like(root.get("bookName"), "%" + searchQuery + "%"),
					builder.like(root.get("bookPublisher"), "%" + searchQuery + "%"),
					builder.like(root.get("bookAuthor"), "%" + searchQuery + "%")));
		}

		// Add conditions based on provided parameters
		if (categoryNames != null && !categoryNames.isEmpty()) {
			spec = spec.and((root, query, builder) -> root.join("category").get("name").in(categoryNames));
		}
		if (bookName != null && !bookName.isEmpty()) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("bookName"), "%" + bookName + "%"));
		}
		if (bookPublishers != null && !bookPublishers.isEmpty()) {
			spec = spec.and((root, query, builder) -> root.get("bookPublisher").in(bookPublishers));
		}
		if (bookAuthors != null && !bookAuthors.isEmpty()) {
			spec = spec.and((root, query, builder) -> root.get("bookAuthor").in(bookAuthors));
		}
		if (minPrice != null && minPrice > 0) {
			spec = spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("bookPrice"), minPrice));
		}
		if (maxPrice != null && maxPrice < Double.MAX_VALUE) {
			spec = spec.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("bookPrice"), maxPrice));
		}

		// Construct Pageable object with sorting
		Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		// Perform the query using the specification
		Page<Book> bookPage = bookRepository.findAll(spec, pageable);

		// Convert Page<Book> to Page<BookDTO>
		return bookPage.map(book -> modelMapper.map(book, BookDTO.class));
	}

	//View By BookID
	public BookDTO viewBookById(Long bookid) {
		Book findById = bookRepository.findById(bookid)
				.orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookid + " not found"));
		return  convertToDto(findById);
	}

	// Delete Book By Id
	public String deleteProduct(Long bookid) {
		Book bookId = bookRepository.findById(bookid)
				.orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookid + " not found"));
		bookRepository.delete(bookId);
		return "book Deleted successfully"+bookid;
	}

	// update Book By Id
	
	
	
	

	// Entity to Dto
	private BookDTO convertToDto(Book book) {
		return modelMapper.map(book, BookDTO.class);
	}

	// Dto to entity
	private Book convertToEntity(BookDTO bookDTO) {
		return modelMapper.map(bookDTO, Book.class);
	}

}
