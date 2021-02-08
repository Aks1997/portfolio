package com.portfolio.base.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.CommonConstants;
import com.portfolio.base.common.ResponseData;
import com.portfolio.base.models.User;
import com.portfolio.base.services.UserService;

@RestController
@RequestMapping("/rest/user")
public class UserController {
	
	@Autowired
	private UserService userServiceImpl;
	
	@RequestMapping(value = "/profileDetails/{userId}", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<Map<String,Object>> getProfileDetails(@PathVariable Long userId) throws BaseException {
		Map<String, Object> details = userServiceImpl.getProfileDetails(userId);
		return new ResponseEntity<Map<String, Object>>(details, HttpStatus.OK);
	}
	
	@RequestMapping(value="/upload/image/{userId}", method= RequestMethod.POST)
	@ResponseBody
	private ResponseEntity<Map<String, Object>> uploadUserImage(@RequestHeader(CommonConstants.ACCESS_TOKEN) String auth,
			@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws BaseException{
		Boolean response= userServiceImpl.uploadUserImage(auth, userId, file);
		Map<String, Object> result=new HashMap<>();
		result.put(CommonConstants.RESULTS, response);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/update/details/{userId}", method= RequestMethod.PUT)
	@ResponseBody
	private ResponseEntity<Map<String, Object>> updateUserDetails(@RequestHeader(CommonConstants.ACCESS_TOKEN) String auth,
			@PathVariable Long userId, @RequestBody Map<String, Object> details) throws BaseException{
		Map<String, Object> response= userServiceImpl.updateUserDetails(auth, userId, details);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
}
