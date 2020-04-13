package com.portfolio.base.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.CommonConstants;
import com.portfolio.base.common.ResponseData;
import com.portfolio.base.models.User;
import com.portfolio.base.services.UserService;

@RestController
@RequestMapping("rest/authenticate")
public class AuthenticationController {

	@Autowired
	private UserService userServiceImpl;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<ResponseData<User>> login(@RequestHeader("Authorization") String auth) {
		Map<String, Object> result= userServiceImpl.logInUser(auth);
		ResponseData<User> response= new ResponseData((User)result.get(CommonConstants.RESULTS), (String)result.get(CommonConstants.MESSAGE),
				(boolean)result.get(CommonConstants.ERROR));
		return new ResponseEntity<ResponseData<User>>(response, (HttpStatus)result.get(CommonConstants.STATUS));
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	private ResponseEntity<Map<String, Object>> register(@RequestBody Map<Object, Object> formFields) throws BaseException{
		Boolean response;
		try {
			response= userServiceImpl.registerUser(formFields);
		}catch(Exception exc) {
			throw new BaseException(exc.getMessage());
		}
		Map<String, Object> result= new HashMap<>();
		result.put("result", response);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CREATED);
	}
}
