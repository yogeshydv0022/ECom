package com.work.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.work.exception.ResourceNotFoundException;
import com.work.payload.response.CartDto;
import com.work.payload.response.ItemRequest;
import com.work.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

//	@PostMapping("/")
//	public ResponseEntity<CartDto> addtoCart(@RequestBody ItemRequest itemRequest, Principal principal) {
//		String email = principal.getName();
//		System.out.println(email);
//		CartDto addItem = cartService.addItem(itemRequest, principal.getName());
//
//		return new ResponseEntity<CartDto>(addItem, HttpStatus.OK);
//	}
	
	@PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody ItemRequest itemRequest, Principal principal) {
        try {
            CartDto cartDto = cartService.addToCart(itemRequest, principal);
            System.out.println("Cart Dto :"+cartDto);
            return ResponseEntity.ok(cartDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
	// create method for getting cart
	@GetMapping("/")
	public ResponseEntity<CartDto> getAllCart(Principal principal) {
		CartDto getcartAll = cartService.getcartAll(principal.getName());
		return new ResponseEntity<CartDto>(getcartAll, HttpStatus.ACCEPTED);
	}

	@GetMapping("/{cartId}")
	public ResponseEntity<CartDto> getCartById(@PathVariable long cartId) {

		System.out.println(cartId);
		CartDto cartByID =cartService.getCartByID(cartId);
		return new ResponseEntity<CartDto>(cartByID, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CartDto>deleteCartItemFromCart(@PathVariable long pid,Principal p){
		
		CartDto remove =cartService.removeCartItemFromCart(p.getName(),pid);
		return new ResponseEntity<CartDto>(remove,HttpStatus.UPGRADE_REQUIRED);
	}
}
