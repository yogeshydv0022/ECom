package com.work.payload.response;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
	private long cartId;
	private Set<CartItemDto> items = new HashSet<>();
	private UserResponse user;
	
}