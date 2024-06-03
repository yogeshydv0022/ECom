package com.work.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.work.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

}
