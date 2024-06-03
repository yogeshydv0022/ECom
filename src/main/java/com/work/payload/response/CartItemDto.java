package com.work.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
	private long cartItemId;
	private int quantity;
	private double totalprice;
//	@JsonIgnore
	private CartDto cart;
	private BookDTO book;
}