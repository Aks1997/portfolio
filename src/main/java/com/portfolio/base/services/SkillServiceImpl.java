package com.portfolio.base.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.portfolio.base.common.CommonConstants;
import com.portfolio.base.models.LookUp;

public class SkillServiceImpl implements SkillService {

	@Autowired
	LookUpService lookUpServiceImpl;
	
	@Override
	public List<Map<String, Object>> getSkillSuggestions(String term) {
		List<LookUp> lookups= lookUpServiceImpl.getLookUpByValueLike("skill",term, 0, 100);
		List<Map<String, Object>> result= new ArrayList<>();
		for(LookUp lookUp : lookups) {
			Map<String, Object> data= new HashMap<>();
			data.put(CommonConstants.ID, lookUp.getId());
			data.put(CommonConstants.VALUE, lookUp.getValue());
			
			result.add(data);
		}
		return result;
	}

}
