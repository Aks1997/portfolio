package com.portfolio.base.services;

import java.util.List;
import java.util.Map;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.models.User;

public interface UserService {
	List<User> getAllUsers();
	Map<String, Object> logInUser(String auth);
	Boolean registerUser(Map<Object, Object> formFields) throws BaseException;
}
