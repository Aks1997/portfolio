package com.portfolio.base.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.portfolio.base.models.LookUp;
import com.portfolio.base.services.HibernatePersistanceService;

@Repository
public class LookUpDao {

	@Autowired
	private HibernatePersistanceService hibernatePersistanceServiceImpl;
	
	public List<LookUp> getLookUpByValueLike(String attr, String term, int skip, int limit) {
		String sql="FROM LookUp look where look.attr=:attr and look.value like :term order by look.id asc";
		Map<String, Object> namedParams=new HashMap<>();
		namedParams.put("term", term);
		namedParams.put("attr", attr);
		List<LookUp> lookups=hibernatePersistanceServiceImpl.executeQuery(sql, namedParams, skip, limit);
		if(lookups==null){
			lookups= new ArrayList<LookUp>();
		}
		return lookups;
	}

}
