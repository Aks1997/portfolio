package com.portfolio.base.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Component
@Transactional(propagation = Propagation.REQUIRED)
public class HibernatePersistanceServiceImpl implements HibernatePersistanceService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List executeQuery(String sql, Map<String, Object> namedParams, int firstResult, int maxResult) {
		Query query= entityManager.createQuery(sql);
		if(namedParams!=null) {
			for (String name : namedParams.keySet()) {
				Object param = namedParams.get(name);
				if (param instanceof Collection) {
					query.setParameter(name, (Collection) param);
				} else {
					query.setParameter(name, param);
				}
			}
		}
		if (firstResult > 0) {
			query.setFirstResult(firstResult);
		}
		if (maxResult > 0)
			query.setMaxResults(maxResult);

		List listResults = query.getResultList();
		return listResults;
	}

}
