package com.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.work.exception.ResourceNotFoundException;
import com.work.model.Role;
import com.work.model.User;
import com.work.payload.request.LoginRequest;
import com.work.payload.request.RegisterRequest;
import com.work.payload.response.UserResponse;
import com.work.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ModelMapper mapper;

	public UserResponse userSignup(RegisterRequest input) {
		User user = User.builder()
				.firstname(input.getFirstname())
				.lastname(input.getLastname())
				.email(input.getEmail())
				.phone(input.getPhone())
				.password(passwordEncoder.encode(input.getPassword()))
				.role(Role.ROLE_USER)
				.build();
		userRepository.save(user);
//		UserResponse response = new UserResponse();
//		response.setId(user.getId());
//		response.setFirstname(user.getFirstname());
//		response.setLastname(user.getLastname());
//		response.setEmail(user.getEmail());
//		response.setPassword(user.getPassword());
//		response.setPhone(user.getPhone());
//		response.setCreatedAt(user.getCreatedAt());
//		response.setAuthorities(user.getAuthorities().toString());
		return convertToDto(user);
	}

	public UserResponse adminSignup(RegisterRequest input) {
		User user = User.builder()
				.firstname(input.getFirstname())
				.lastname(input.getLastname())
				.email(input.getEmail())
				.phone(input.getPhone())
				.password(passwordEncoder.encode(input.getPassword()))
				.role(Role.ROLE_ADMIN)
				.build();
		userRepository.save(user);
//		UserResponse response = new UserResponse();
//		response.setId(user.getId());
//		response.setFirstname(user.getFirstname());
//		response.setLastname(user.getLastname());
//		response.setEmail(user.getEmail());
//		response.setPassword(user.getPassword());
//		response.setPhone(user.getPhone());
//		response.setCreatedAt(user.getCreatedAt());
//		response.setAuthorities(user.getAuthorities().toString());
		return convertToDto(user);
	}

	public UserResponse superAdminSignup(RegisterRequest input) {
		User user = User.builder()
				.firstname(input.getFirstname())
				.lastname(input.getLastname())
				.email(input.getEmail())
				.phone(input.getPhone())
				.password(passwordEncoder.encode(input.getPassword()))
				.role(Role.ROLE_SUPER_ADMIN)
				.build();
		userRepository.save(user);
//		UserResponse response = new UserResponse();
//		response.setId(user.getId());
//		response.setFirstname(user.getFirstname());
//		response.setLastname(user.getLastname());
//		response.setEmail(user.getEmail());
//		response.setPassword(user.getPassword());
//		response.setPhone(user.getPhone());
//		response.setCreatedAt(user.getCreatedAt());
//		response.setAuthorities(user.getAuthorities().toString());
		return convertToDto(user);
	}

	public User authenticateLogin(LoginRequest input) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

		return userRepository.findByEmail(input.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User Email Is is Not found" + input.getEmail()));

	}

	public List<User> allUsers() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		return users;
	}

	public List<UserResponse> getUsers() {

		List<User> categories = userRepository.findAll();
		return categories.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public UserResponse update(RegisterRequest request, long userId) {
		User u = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found by this id"));
//		u.setFirstname(request.getFirstname());
//		u.setLastname(request.getLastname());
//		u.setEmail(request.getEmail());
//		u.setPassword(request.getPassword());
//		u.setPhone(request.getPhone());
		User Updateuser = userRepository.save(u);

		return convertToDto(Updateuser);
	}

	// Entity to Dto
	private UserResponse convertToDto(User user) {
		return mapper.map(user, UserResponse.class);
	}

	// Dto to entity
//	private User convertToEntity(UserResponse response) {
//		return mapper.map(response, User.class);
//	}

}
