package com.portfolio.base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portfolio.base.dao.LookUpDao;
import com.portfolio.base.models.LookUp;
import com.portfolio.base.repositories.LookUpRepository;

@Service
public class LookUpServiceImpl implements LookUpService{

	@Autowired
	LookUpRepository lookUpRepository;
	
	@Autowired
	LookUpDao lookUpDao;
	
	@Override
	public List<LookUp> getLookUpByAttr(String attr) {
		return lookUpRepository.getLookUpByAttr(attr);
	}

	@Override
	public List<LookUp> getLookUpByValueLike(String attr, String term, int skip, int limit) {
		return lookUpDao.getLookUpByValueLike(attr, term, skip, limit);
	}

}
