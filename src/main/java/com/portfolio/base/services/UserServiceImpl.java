package com.portfolio.base.services;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.BaseUtil;
import com.portfolio.base.common.CommonConstants;
import com.portfolio.base.common.FileManager;
import com.portfolio.base.common.PasswordManager;
import com.portfolio.base.common.ReflectionManager;
import com.portfolio.base.models.LookUp;
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
	
	@Autowired 
	LookUpService lookUpServiceImpl;
	
	@Value("${app.upload.path}")
	private String uploadPath;
	
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private ReflectionManager reflectionManager;
	
	@Override
	public Map<String, Object> getProfileDetails(Long userId) throws BaseException {
		Map<String, Object> details=new HashMap<>();
		User user= userRepository.findUserById(userId);
		if(user!=null) {
			List<Map<String, Object>> detailsAttr= createDetailsAttr(user);
			
			try {
				String imagePath=user.getImagePath();
				if(imagePath!=null && !imagePath.equals("")) {
					byte[] fileContent = fileManager.getFileContentFromPath(imagePath);
					details.put(CommonConstants.USER_IMAGE, fileContent);
				}
			} catch (IOException e) {
				
			}
			details.put(CommonConstants.USER_ATTR, detailsAttr);
		}else {
			throw new BaseException(HttpStatus.NOT_FOUND,CommonConstants.USER_NOT_FOUND);
		}
		return details;
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
					response.put(CommonConstants.USER_ID, user.getId());
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
	
	private List<Map<String, Object>> createDetailsAttr(User user){
		List<Map<String, Object>> attrs=new ArrayList<>();
		
		attrs.add(BaseUtil.createAttribute(CommonConstants.NAME, CommonConstants.NAME, user.getName(), 
				CommonConstants.TYPE_TEXTFIELD, false, false, null, false));
		
		attrs.add(BaseUtil.createAttribute(CommonConstants.PROFFESSION, CommonConstants.PROFFESSION, user.getProfession(), 
				CommonConstants.TYPE_AUTOCOMPLETE, false, false, 
				getOptions(CommonConstants.PROFFESSION.toLowerCase()), false));
		
		attrs.add(BaseUtil.createAttribute(CommonConstants.STATE, CommonConstants.STATE, user.getState(), 
				CommonConstants.TYPE_DROPDOWN, false, false, 
				getOptions(CommonConstants.STATE.toLowerCase()), false));
		
		attrs.add(BaseUtil.createAttribute(CommonConstants.GENDER, CommonConstants.GENDER, user.getGender(), 
				CommonConstants.TYPE_DROPDOWN, false, false, 
				getOptions(CommonConstants.GENDER.toLowerCase()), false));
		
		attrs.add(BaseUtil.createAttribute(CommonConstants.ABOUT, CommonConstants.ABOUT, user.getAbout(), 
				CommonConstants.TYPE_TEXTAREA, false, false, null, false));
		
		return attrs;
	}
	
	private List<Map<String, Object>> getOptions(String key){
		List<LookUp> lookups= lookUpServiceImpl.getLookUpByAttr(key);  
		List<Map<String, Object>> options= new ArrayList<>();
		
		if(lookups!=null && lookups.size()>0) {
			for(LookUp lookup : lookups) {
				Map<String, Object> option= new HashMap<>();
				option.put(CommonConstants.KEY, lookup.getId());
				option.put(CommonConstants.VALUE, lookup.getValue());
				
				options.add(option);
			}
		}
		return options;
	}

	@Override
	public Boolean uploadUserImage(String auth, Long userId, MultipartFile file) throws BaseException {
		String userName= jwtManager.getUserNameFromJwt(auth);
		
		User user= userRepository.findUserByUserName(userName);
		
		if(user.getId()==userId) {
			try {
				String imagePath= fileManager.saveFile(file, "/uploads/"); 
                user.setImagePath(imagePath);
				
				userRepository.save(user);
			} catch (Exception e) {
				throw new BaseException(CommonConstants.ERROR_UPLOAD_IMAGE);
			}
		}else {
			throw new BaseException(HttpStatus.UNAUTHORIZED, CommonConstants.INVALID_TOKEN);
		}
		
		return true;
	}

	@Override
	public Map<String, Object> updateUserDetails(String auth, Long userId, Map<String, Object> details) throws BaseException {
		String userName= jwtManager.getUserNameFromJwt(auth);
		
		User user= userRepository.findUserByUserName(userName);
		
		if(user.getId()==userId) {
			
			for(Map.Entry<String, Object> attr: details.entrySet()) {
				String property= attr.getKey();
				Object value= attr.getValue();
				
				try {
					reflectionManager.invokeSetter(user, property, value);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| IntrospectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			userRepository.save(user);
		}else {
			throw new BaseException(HttpStatus.UNAUTHORIZED, CommonConstants.INVALID_TOKEN);
		}
		
		Map<String, Object> attrs=new HashMap<>();
		List<Map<String, Object>> detailsAttr= createDetailsAttr(user);
		attrs.put(CommonConstants.USER_ATTR, detailsAttr);
		
		return attrs;
	}

	@Override
	public Boolean existsByUserId(Long id) {
		return userRepository.existsById(id);
	}

	@Override
	public Boolean checkUserIdForUserName(String userName, Long id) {
		return userRepository.checkUserExistsByUserNameAndUserId(userName, id)>0?true:false;
	}

	@Override
	public User getUserByUserName(String userName) {
		return userRepository.findUserByUserName(userName);
	}
}
