package com.portfolio.base.services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.BaseUtil;
import com.portfolio.base.common.CommonConstants;
import com.portfolio.base.common.PasswordManager;
import com.portfolio.base.models.User;
import com.portfolio.base.repositories.UserRepository;
import com.portfolio.base.security.JwtManager;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordManager passwordManager;
	
	@Autowired
	private JwtManager jwtManager;
	
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Map<String, Object> logInUser(String auth) {
		Map<String, Object> response=new HashMap<>();
		String[] credentials= resolveBasicAuthString(auth);
		if(credentials!=null && credentials.length==2) {
			User user=userRepository.findUserByUserName(credentials[0]);
			if(user==null) {
				response.put(CommonConstants.MESSAGE, CommonConstants.INCORRECT_USERNAME);
				response.put(CommonConstants.STATUS, HttpStatus.UNAUTHORIZED);
				response.put(CommonConstants.ERROR, true);
			}
			else if(passwordManager.compareCredP(credentials[1], user.getPassword())) {
				String accessToken= jwtManager.createJwt(user.getUserName());
				if(accessToken!=null && !accessToken.equals("")) {
					response.put(CommonConstants.ACCESS_TOKEN, accessToken);
					response.put(CommonConstants.USER_NAME, user.getUserName());
					response.put(CommonConstants.MESSAGE, CommonConstants.SUCCESS);
					response.put(CommonConstants.STATUS, HttpStatus.OK);
					response.put(CommonConstants.ERROR, false);
				}else {
					response.put(CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR);
					response.put(CommonConstants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
					response.put(CommonConstants.ERROR, true);
				}
			}
			else {
				response.put(CommonConstants.MESSAGE, CommonConstants.INCORRECT_PASSWORD);
				response.put(CommonConstants.STATUS, HttpStatus.UNAUTHORIZED);
				response.put(CommonConstants.ERROR, true);
			}
		}
		else {
			response.put(CommonConstants.MESSAGE, CommonConstants.BAD_REQUEST);
			response.put(CommonConstants.STATUS, HttpStatus.BAD_REQUEST);
			response.put(CommonConstants.ERROR, true);
		}
		return response;
	}

	private String[] resolveBasicAuthString(String auth) {
		if (auth != null && auth.toLowerCase().startsWith("basic")) {
		    // Authorization: Basic base64credentials
		    String base64Credentials = auth.substring("Basic".length()).trim();
		    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		    // credentials = username:password
		    String[] values = credentials.split(":", 2);
		    return values;
		}
		return null;
	}

	@Override
	public Boolean registerUser(Map<Object, Object> formFields) throws BaseException {
		String userName= BaseUtil.resolveBase64ToString(formFields.get(CommonConstants.USER_NAME).toString());
		
		if(userRepository.existsByUserName(userName)) {
			throw new BaseException(CommonConstants.USER_ALREADY_EXISTS);
		}
		
		String password= BaseUtil.resolveBase64ToString(formFields.get(CommonConstants.PASSWORD).toString());
		
		password= passwordManager.encodeCredP(password);
		
		User user= new User();
		user.setUserName(userName);
		user.setPassword(password);
		user.setCreatedOn(new Date());
		
		userRepository.save(user);
		
		return true;
	}
}
