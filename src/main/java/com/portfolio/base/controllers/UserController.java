package com.portfolio.base.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.base.common.ResponseData;
import com.portfolio.base.models.User;
import com.portfolio.base.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userServiceImpl;
	
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<List<User>> getUsers() {
		List<User> users = userServiceImpl.getAllUsers();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
}
