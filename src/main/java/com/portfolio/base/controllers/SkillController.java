package com.portfolio.base.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.base.services.SkillService;

@RestController
@RequestMapping("rest/skill")
public class SkillController {

	@Autowired
	SkillService skillServiceImpl; 
	
	@RequestMapping(value="/suggestions", method=RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<List<Map<String, Object>>> getSkillsSuggestions(@RequestParam String term){
		List<Map<String, Object>> result= skillServiceImpl.getSkillSuggestions(term);
		return new ResponseEntity<List<Map<String, Object>>>(result, HttpStatus.OK);
	}
}
