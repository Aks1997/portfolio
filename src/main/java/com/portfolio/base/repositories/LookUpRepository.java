package com.portfolio.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.portfolio.base.models.LookUp;

public interface LookUpRepository extends JpaRepository<LookUp, Long> {

	List<LookUp> getLookUpByAttr(String attr);
}
