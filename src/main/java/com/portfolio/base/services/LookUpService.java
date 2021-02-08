package com.portfolio.base.services;

import java.util.List;

import com.portfolio.base.models.LookUp;

public interface LookUpService {
	List<LookUp> getLookUpByAttr(String attr);
	
	List<LookUp> getLookUpByValueLike(String attr, String term, int skip, int limit);
}
