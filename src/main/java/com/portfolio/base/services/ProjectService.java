package com.portfolio.base.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.CommonConstants;

public interface ProjectService {
	public Map<String, Object> getProjectsByuserId(Long userId, int skip, int limit) throws BaseException, IOException;
	public Map<String, Object> uploadProjectImages(String auth,Long userId, Long projectId, MultipartFile[] images) throws BaseException, IOException;
	public Map<String, Object> uploadProjectDetails(String auth,Long userId, Long projectId, Map<String, Object> details) throws BaseException, IOException;
	public Map<String, Object> deleteProjectById(Long projectId);
	public Map<String, Object> deleteProjectImage(Long projectId, Map<String, Object> image) throws IOException;
}
