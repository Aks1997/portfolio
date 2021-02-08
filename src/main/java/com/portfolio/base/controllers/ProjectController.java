package com.portfolio.base.controllers;

import java.io.IOException;
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

import com.portfolio.base.common.AuthManager;
import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.CommonConstants;
import com.portfolio.base.services.ProjectService;

@RestController
@RequestMapping("/rest/project")
public class ProjectController {

	@Autowired
	private ProjectService projectServiceImpl;
	
	@Autowired
	private AuthManager authManager;
	
	@RequestMapping(value="/{userId}", method= RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<Map<String, Object>> getProjectsByUserId(@PathVariable Long userId, @RequestParam int skip,
			@RequestParam int limit) throws BaseException, IOException{
		Map<String, Object> projects= projectServiceImpl.getProjectsByuserId(userId, skip, limit);
		return new ResponseEntity<Map<String, Object>>(projects, HttpStatus.OK);
	}
	
	@RequestMapping(value="/upload/images/{userId}/{projectId}", method= RequestMethod.POST)
	@ResponseBody
	private ResponseEntity<Map<String, Object>> uploadProjectImages(
			@RequestHeader(CommonConstants.ACCESS_TOKEN) String auth,
			@PathVariable Long userId, @PathVariable Long projectId, 
			@RequestParam(value="images") MultipartFile[] images) throws BaseException, IOException{
		if(projectId==-1)
			projectId=null;
		Map<String, Object> result= projectServiceImpl.uploadProjectImages(auth, userId, projectId, images);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/upload/details/{userId}/{projectId}", method= RequestMethod.POST)
	@ResponseBody
	private ResponseEntity<Map<String, Object>> uploadProjectDetails(
			@RequestHeader(CommonConstants.ACCESS_TOKEN) String auth,
			@PathVariable Long userId, @PathVariable Long projectId,
			@RequestBody Map<String, Object> details) throws BaseException, IOException{
		Map<String, Object> project= projectServiceImpl.uploadProjectDetails(auth, userId, projectId, details);
		return new ResponseEntity<Map<String, Object>>(project, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/delete/{userId}/{projectId}", method= RequestMethod.PUT)
	@ResponseBody
	private ResponseEntity<Map<String, Object>> deleteProject(@RequestHeader(CommonConstants.ACCESS_TOKEN) String auth,
			@PathVariable Long userId, @PathVariable Long projectId) throws BaseException{
		authManager.authenticateUrl(auth, userId);
		Map<String, Object> result= projectServiceImpl.deleteProjectById(projectId);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteImages/{userId}/{projectId}", method=RequestMethod.POST)
	@ResponseBody
	private ResponseEntity<Map<String, Object>> deleteImage(@RequestHeader(CommonConstants.ACCESS_TOKEN) String auth,
			@PathVariable Long userId, @PathVariable Long projectId,@RequestBody Map<String, Object> image) throws BaseException, IOException{
		authManager.authenticateUrl(auth, userId);
		Map<String, Object> result= projectServiceImpl.deleteProjectImage(projectId, image);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
}
