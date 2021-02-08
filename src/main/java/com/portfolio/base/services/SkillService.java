package com.portfolio.base.services;

import java.util.List;
import java.util.Map;

public interface SkillService {
	public List<Map<String, Object>> getSkillSuggestions(String term);
}
