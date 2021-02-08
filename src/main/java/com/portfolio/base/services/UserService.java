package com.portfolio.base.services;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.models.User;

public interface UserService {
	Map<String, Object> getProfileDetails(Long userId) throws BaseException;
	Map<String, Object> logInUser(String auth);
	Boolean registerUser(Map<Object, Object> formFields) throws BaseException;
	Boolean uploadUserImage(String auth, Long userId, MultipartFile file) throws BaseException;
	Map<String, Object> updateUserDetails(String auth, Long userId, Map<String, Object> details) throws BaseException;
	Boolean existsByUserId(Long id);
	Boolean checkUserIdForUserName(String userName, Long id);
	User getUserByUserName(String userName);
}
