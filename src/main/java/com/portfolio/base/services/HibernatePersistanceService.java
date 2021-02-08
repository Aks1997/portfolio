package com.portfolio.base.services;

import java.util.List;
import java.util.Map;

public interface HibernatePersistanceService {
	public List executeQuery(String sql, Map<String, Object> namedParams, int firstResult, int maxResult);
}
