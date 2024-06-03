package com.work.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.work.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
	
	Page<Book> findAll(Specification<Book> spec, Pageable pageable);

	
}
