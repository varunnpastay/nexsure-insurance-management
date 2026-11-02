package com.Dcl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Dcl.dto.UserDto;
import com.Dcl.entity.User;
import com.Dcl.exception.AppException;
import com.Dcl.repo.PurchaseRepository;
import com.Dcl.repo.UserRepository;
import com.Dcl.request.LoginRequest;
import com.Dcl.request.RegisterRequest;
import com.Dcl.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository urepo;

	@Autowired
	private PurchaseRepository purchaseRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public UserDto register(RegisterRequest request) {
		logger.info("Register request for email: {}", request.getEmail());
		//validating whether the user is existing user or not!
		User alreadyExists=urepo.findByEmail(request.getEmail()).orElse(null);
		if(alreadyExists!=null) {
			logger.warn("Registration failed - email already exists: {}", request.getEmail());
			throw new AppException("User already exists!", HttpStatus.CONFLICT);
		}

		//transfering the data from request to entity
		User user=mapper.map(request, User.class);
		user.setRole("CUSTOMER");
		user=urepo.save(user);
		logger.info("User registered successfully with id: {}", user.getUserId());

		//tranfering the data from entity to dto
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto login(LoginRequest request) {
		//never log the password!
		logger.info("Customer login attempt: {}", request.getEmail());
		//Validating the user with email
		User user=urepo.findByEmail(request.getEmail()).orElseThrow(()->{
			logger.warn("Login failed - user not found: {}", request.getEmail());
			return new AppException("User Not Found!", HttpStatus.NOT_FOUND);
		});
		//Password validation
		if(!user.getPassword().equals(request.getPassword())) {
			logger.warn("Login failed - incorrect password for: {}", request.getEmail());
			throw new AppException("Incorrect Password!", HttpStatus.UNAUTHORIZED);
		}
		//customer portal - admin is not allowed here
		if(!user.getRole().equals("CUSTOMER")) {
			logger.warn("Login blocked - admin tried customer portal: {}", request.getEmail());
			throw new AppException("Admin must login from the Admin Portal!", HttpStatus.FORBIDDEN);
		}
		logger.info("Customer logged in: {} (id: {})", user.getEmail(), user.getUserId());
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto adminLogin(LoginRequest request) {
		logger.info("Admin login attempt: {}", request.getEmail());
		User user=urepo.findByEmail(request.getEmail()).orElseThrow(()->{
			logger.warn("Admin login failed - not found: {}", request.getEmail());
			return new AppException("Admin Not Found!", HttpStatus.NOT_FOUND);
		});
		if(!user.getPassword().equals(request.getPassword())) {
			logger.warn("Admin login failed - incorrect password for: {}", request.getEmail());
			throw new AppException("Incorrect Password!", HttpStatus.UNAUTHORIZED);
		}
		//admin portal - only ADMIN role allowed
		if(!user.getRole().equals("ADMIN")) {
			logger.warn("Admin login denied - not an admin: {}", request.getEmail());
			throw new AppException("Access denied! Not an admin account.", HttpStatus.FORBIDDEN);
		}
		logger.info("Admin logged in: {}", user.getEmail());
		return mapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAll() {
		List<User> userList=urepo.findAll();
		logger.debug("Fetched {} users", userList.size());
		return userList.stream()
		        .map(u->mapper.map(u, UserDto.class))
		        .collect(Collectors.toList());
	}

	@Override
	public void deleteUserById(Integer userId) {
		logger.info("Delete request for user id: {}", userId);
		User user=urepo.findById(userId).orElseThrow(()->{
			logger.warn("Delete failed - user not found: {}", userId);
			return new AppException("User Not Found!", HttpStatus.NOT_FOUND);
		});
		if(user.getRole().equals("ADMIN")) {
			logger.warn("Delete blocked - tried to delete admin id: {}", userId);
			throw new AppException("Admin can't be deleted!", HttpStatus.BAD_REQUEST);
		}
		if(purchaseRepo.existsByUser_UserId(userId)) {
			logger.warn("Delete blocked - user {} has purchases", userId);
			throw new AppException("User has purchased policies, can't be deleted!", HttpStatus.CONFLICT);
		}
		urepo.deleteById(userId);
		logger.info("User deleted: {}", userId);
	}

}
