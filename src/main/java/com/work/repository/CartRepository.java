package com.work.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.work.model.Cart;
import com.work.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	public Optional<Cart> findByUser(User user);

}
