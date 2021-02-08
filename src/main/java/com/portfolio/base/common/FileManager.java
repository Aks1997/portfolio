package com.portfolio.base.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManager {

	@Value("${app.upload.path}")
	private String uploadPath;
	
	public String saveFile(MultipartFile file, String dir) throws IOException {
		String date= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
		String fileName= date+ file.getOriginalFilename();
		
		String dirPath= uploadPath+dir;
		
		Path path = Paths.get(dirPath+fileName);
        if(! new File(dirPath).exists()){
            new File(dirPath).mkdir();
        }
        
        Files.write(path, file.getBytes());
		
		return dirPath+fileName;
	}
	
	public boolean deleteFile(String path) {
		File file= new File(path);
		return file.delete();
	}
	
	public byte[] getFileContentFromPath(String path) throws IOException {
		File file=new File(path);
		byte[] fileContent= Files.readAllBytes(file.toPath());
		return fileContent;
	}
}
