package com.portfolio.base.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.portfolio.base.models.User;
import com.portfolio.base.security.JwtManager;
import com.portfolio.base.services.UserService;

@Component
public class AuthManager {

	@Autowired
	private JwtManager jwtManager;
	
	@Autowired
	private UserService userServiceImpl;
	
	public void authenticateUrl(String auth, Long userId) throws BaseException {
		String userName= jwtManager.getUserNameFromJwt(auth);
		
		User user= userServiceImpl.getUserByUserName(userName);
		
		if(userId!=user.getId()) {
			throw new BaseException(HttpStatus.UNAUTHORIZED, CommonConstants.INVALID_TOKEN);
		}
	}
}
