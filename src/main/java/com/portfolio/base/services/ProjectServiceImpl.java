package com.portfolio.base.services;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.BaseUtil;
import com.portfolio.base.common.CommonConstants;
import com.portfolio.base.common.FileManager;
import com.portfolio.base.common.ReflectionManager;
import com.portfolio.base.models.Project;
import com.portfolio.base.models.User;
import com.portfolio.base.repositories.ProjectRepository;
import com.portfolio.base.security.JwtManager;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private JwtManager jwtManager;
	
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private ReflectionManager reflectionManager;
	
	@Autowired
	private HibernatePersistanceService hibernatePersistanceServiceImpl;
	
	@Override
	public Map<String, Object> getProjectsByuserId(Long userId, int skip, int limit) throws BaseException, IOException {
		Boolean userExists= userServiceImpl.existsByUserId(userId);
		Map<String, Object> response= new HashMap<>();
		List<Map<String, Object>> result= new ArrayList<>();
		
		if(userExists) {
			String sql="FROM Project pr where pr.user.id=:id order by id desc";
			Map<String, Object> namedParams= new HashMap<>();
			namedParams.put("id", userId);
			List<Project> projects= hibernatePersistanceServiceImpl.executeQuery(sql, namedParams, skip, limit);
			Long count= projectRepository.countProjectsOfUserId(userId);
			for(Project project: projects) {
				result.add(createProjectObject(project));
			}
			response.put(CommonConstants.PROJECTS, result);
			response.put(CommonConstants.COUNT, count);
		}else {
			throw new BaseException(HttpStatus.NOT_FOUND,CommonConstants.USER_NOT_FOUND);
		}
		return response;
	}
	
	private List<Map<String, Object>> createProjectDetails(Project project){
		List<Map<String, Object>> attrs=new ArrayList<>();
		attrs.add(BaseUtil.createAttribute(CommonConstants.TITLE, CommonConstants.TITLE, project.getTitle(), 
				CommonConstants.TYPE_TEXTFIELD, false, false, null, false));
		attrs.add(BaseUtil.createAttribute(CommonConstants.LINK, CommonConstants.LINK, project.getLink(), 
				CommonConstants.TYPE_TEXTFIELD, false, false, null, true));
		attrs.add(BaseUtil.createAttribute(CommonConstants.DESCRIPTION, CommonConstants.DESCRIPTION, project.getDescription(), 
				CommonConstants.TYPE_TEXTAREA, false, false, null, false));
		return attrs;
	}

	@Override
	public Map<String, Object> uploadProjectImages(String auth, Long userId, Long projectId,
			MultipartFile[] images) throws BaseException, IOException {
		String userName= jwtManager.getUserNameFromJwt(auth);
		
		User user= userServiceImpl.getUserByUserName(userName);
		String[] imagePaths;
		Project project;
		if(userId==user.getId()) {
			if(projectId==null) {
				project=new Project();
				project.setUser(user);
			}else {
				project= projectRepository.getOne(projectId);
			}
			String[] oldImages= project.getImagePath();
			int length= images.length+ (oldImages!=null ? oldImages.length : 0);
			imagePaths=new String[length];
			int index=0;
			for(MultipartFile image: images) {
				String imagePath= fileManager.saveFile(image, "/uploads/");
				imagePaths[index++]=imagePath;
			}
			if(oldImages!=null) {
				for(String path: oldImages) {
					imagePaths[index++]= path;
				}
			}
			project.setImagePath(imagePaths);
			projectRepository.save(project);
		}else {
			throw new BaseException(HttpStatus.UNAUTHORIZED, CommonConstants.INVALID_TOKEN);
		}
		
		return createProjectObject(project);
	}
	
	private Map<String, Object> createProjectObject(Project project) throws IOException{
		List<Map<String, Object>> attrs= createProjectDetails(project);
		String[] imagePaths= project.getImagePath();
		List<Map<String, Object>> result=new ArrayList<>();
		if(imagePaths!=null && imagePaths.length>0) {
			for(String img: imagePaths) {
				byte[] fileContent = fileManager.getFileContentFromPath(img);
				Map<String, Object> imageObject= new HashMap<>();
				imageObject.put(CommonConstants.IMAGE_PATH, img);
				imageObject.put(CommonConstants.IMAGE_CONTENT, fileContent);
				result.add(imageObject);
			}
		}
		Map<String, Object> pr= new HashMap<>();
		pr.put(CommonConstants.IMAGES, result);
		pr.put(CommonConstants.PROJECT_ATTR, attrs);
		pr.put(CommonConstants.ID, project.getId());
		return pr;
	}

	@Override
	public Map<String, Object> uploadProjectDetails(String auth, Long userId, Long projectId,
			Map<String, Object> details) throws BaseException, IOException {
		String userName= jwtManager.getUserNameFromJwt(auth);
		
		User user= userServiceImpl.getUserByUserName(userName);
		Project project;
		if(userId==user.getId()) {
			if(projectId==-1) {
				project=new Project();
				project.setUser(user);
			}else {
				project= projectRepository.getOne(projectId);
			}
			for(Map.Entry<String, Object> attr: details.entrySet()) {
				String property= attr.getKey();
				Object value= attr.getValue();
				
				try {
					reflectionManager.invokeSetter(project, property, value);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| IntrospectionException e) {
					e.printStackTrace();
				}
			}
			projectRepository.save(project);
		}else {
			throw new BaseException(HttpStatus.UNAUTHORIZED, CommonConstants.INVALID_TOKEN);
		}
		return createProjectObject(project);
	}

	@Override
	public Map<String, Object> deleteProjectById(Long projectId) {
		Map<String, Object> result= new HashMap<>();
		try {
			Project project= projectRepository.getOne(projectId);
			String[] imagePath= project.getImagePath();
			if(imagePath!=null) {
				for(String path : imagePath) {
					fileManager.deleteFile(path);
				}
			}
			projectRepository.delete(project);
			result.put(CommonConstants.RESULTS, true);
		}catch(Exception e){
			result.put(CommonConstants.RESULTS, false);
		}
		return result;
	}

	@Override
	public Map<String, Object> deleteProjectImage(Long projectId, Map<String, Object> image) throws IOException {
		Map<String, Object> result= new HashMap<>();
		Project project= projectRepository.getOne(projectId);
		if(project==null) {
			result.put(CommonConstants.ERROR, true);
			result.put(CommonConstants.MESSAGE, "Project doesn't exist");
			return result;
		}
		if(image.containsKey(CommonConstants.IMAGE_PATH)) {
			fileManager.deleteFile((String)image.get(CommonConstants.IMAGE_PATH));
			String[] oldPaths= project.getImagePath();
			String[] newPaths= new String[oldPaths.length-1];
			int index=0;
			for(String str: oldPaths) {
				if(!str.equals((String)image.get(CommonConstants.IMAGE_PATH)) && index<newPaths.length) {
					newPaths[index++]= str;
				}
			}
			project.setImagePath(newPaths);
			projectRepository.save(project);
		}
		return createProjectObject(project);
	}
}
