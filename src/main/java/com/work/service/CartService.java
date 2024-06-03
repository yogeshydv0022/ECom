package com.work.service;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.exception.ResourceNotFoundException;
import com.work.model.Book;
import com.work.model.Cart;
import com.work.model.CartItem;
import com.work.model.User;
import com.work.payload.response.CartDto;
import com.work.payload.response.ItemRequest;
import com.work.repository.BookRepository;
import com.work.repository.CartRepository;
import com.work.repository.UserRepository;

@Service
public class CartService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ModelMapper modelMapper;

//	public CartDto addItem(ItemRequest item,String username){
//		long productId=item.getBookId();
//        int quantity=item.getQuantity();
//        //fetch user
//        User user = userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User not found"));
//       //fetch Product 
//       	Book book=bookRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Not Found"));
//        	
//       	//here we are checking product stock 
//        if(!book.isStock()){
//        	
//        	new ResourceNotFoundException("Book Out of Stock");
//        }
//        
//        // create cartItem with productId and Qnt
//        
//        CartItem cartItem=new CartItem();
//        cartItem.setBook(book);
//        cartItem.setQuantity(quantity);
//        double totaleprice=book.getBookPrice()*quantity;
//        cartItem.setTotalprice(totaleprice);
//        
//        //getting cart from user
//        Cart cart=user.getCart();
//        
//        if(cart==null) {
//        	cart=new Cart();
//        	//
//        	cart.setUser(user);
//        }
//        
//        cartItem.setCart(cart);
//        
//        Set<CartItem> items = cart.getItems();
//        
//        // here we check item is available in CartItem or not 
//        // if CartItem is availabe then we inc Qnt only else
//        // add new product in cartItem
//        //
//        // by default false
//        	AtomicReference<Boolean> flag=new AtomicReference<>(false);
//        	
//        Set<CartItem> newproduct = items.stream().map((i)->{
//          if(i.getBook().getBookId()==book.getBookId()) {
//        	  i.setQuantity(quantity);
//        	  i.setTotalprice(totaleprice);
//        	  flag.set(true);
//          }
//          return i;
//          
//        }).collect(Collectors.toSet());
//        
//        if(flag.get()){
//        	items.clear();
//        	items.addAll(newproduct);
//        	
//        }else {
//        	cartItem.setCart(cart);
//        	items.add(cartItem);
//        }
//        
//        Cart saveCart =cartRepository.save(cart);
//        
//           
//        
//		return  this.modelMapper.map(saveCart,CartDto.class);
//	}
//	

//	 public CartDto addItem(ItemRequest item, String username) {
//	        long productId = item.getBookId();
//	        int quantity = item.getQuantity();
//
//	        // Fetch user
//	        User user = userRepository.findByEmail(username)
//	                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//	        // Fetch Product
//	        Book book = bookRepository.findById(productId)
//	                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
//
//	        // Check if product is in stock
//	        if (!book.isStock()) {
//	            throw new ResourceNotFoundException("Book Out of Stock");
//	        }
//
//	        // Get or create cart for user
//	        Cart cart = user.getCart();
//	        if (cart == null) {
//	            cart = new Cart();
//	            cart.setUser(user);
//	        }
//
//	        // Check if item already exists in cart
//	        boolean itemExists = cart.getItems().stream()
//	                .anyMatch(cartItem -> cartItem.getBook().getBookId() == productId);
//
//	        if (itemExists) {
//	            cart.getItems().forEach(cartItem -> {
//	                if (cartItem.getBook().getBookId() == productId) {
//	                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
//	                    cartItem.setTotalprice(cartItem.getBook().getBookPrice() * cartItem.getQuantity());
//	                }
//	            });
//	        } else {
//	            CartItem newCartItem = new CartItem();
//	            newCartItem.setBook(book);
//	            newCartItem.setQuantity(quantity);
//	            newCartItem.setTotalprice(book.getBookPrice() * quantity);
//	            newCartItem.setCart(cart);
//	            cart.getItems().add(newCartItem);
//	        }
//
//	        // Save cart
//	        Cart savedCart = cartRepository.save(cart);
//
//	        // Map Cart entity back to CartDto and return
//	        return modelMapper.map(savedCart, CartDto.class);
//	    }

	public CartDto addToCart(ItemRequest itemRequest, Principal principal) {
	    String email = principal.getName();
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    System.out.println("this Is USER :"+user);
	    System.out.println("this Is BOOk ID :"+itemRequest.getBookId());

	    Book book = bookRepository.findById(itemRequest.getBookId())
	            .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

	    
	 
	    
	    System.out.println("this Is Book :" +book.getBookName());
	    // Check if the book is in stock
	    if (!book.isStock()) {
	        throw new ResourceNotFoundException("Book is out of stock");
	    }

	    // Create or retrieve the cart for the user
	    Cart cart = user.getCart();
	    if (cart == null) {
	        cart = new Cart();
	        cart.setUser(user);
	    }

	    // Check if the book already exists in the cart
	    boolean bookExistsInCart = false;
	    for (CartItem cartItem : cart.getItems()) {
	        if (cartItem.getBook().getBookId() == book.getBookId()) {
	            // Update the quantity and total price of the existing cart item
	            int newQuantity = cartItem.getQuantity() + itemRequest.getQuantity();
	            cartItem.setQuantity(newQuantity);
	            cartItem.setTotalprice(book.getBookPrice() * newQuantity);
	            bookExistsInCart = true;
	            break;
	        }
	    }

	    // If the book is not already in the cart, create a new cart item
	    if (!bookExistsInCart) {
	        CartItem cartItem = new CartItem();
	        cartItem.setBook(book);
	        cartItem.setQuantity(itemRequest.getQuantity());
	        cartItem.setTotalprice(book.getBookPrice() * itemRequest.getQuantity());
	        cartItem.setCart(cart);
	        cart.getItems().add(cartItem);
	    }

	    // Save the cart
	    cartRepository.save(cart);

	    // Map the cart entity to DTO and return
	    return modelMapper.map(cart, CartDto.class);
	}
	

	public CartDto getcartAll(String email) {
		// find user
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		// find cart
		Cart cart = cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("There is no cart"));

		return this.modelMapper.map(cart, CartDto.class);

	}

	// get cart by CartId

	public CartDto getCartByID(long cartId) {

		// User user=this.userRepo.findByEmail(username).orElseThrow(()->new
		// ResourceNotFoundException("User Not found"));

		Cart findByUserAndcartId = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not Found"));

		return this.modelMapper.map(findByUserAndcartId, CartDto.class);
	}

	public CartDto removeCartItemFromCart(String userName, long bookId) {
		User user = userRepository.findByEmail(userName)
				.orElseThrow(() -> new ResourceNotFoundException("User Not found"));

		Cart cart = user.getCart();
		Set<CartItem> items = cart.getItems();

		boolean removeIf = items.removeIf((i) -> i.getBook().getBookId() == bookId);
		Cart save = cartRepository.save(cart);
		System.out.println(removeIf);
		return this.modelMapper.map(save, CartDto.class);
	}

}