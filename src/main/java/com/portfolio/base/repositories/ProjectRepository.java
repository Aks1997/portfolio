package com.portfolio.base.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portfolio.base.models.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{

	@Query("FROM Project pr where pr.user.id=:id")
	List<Project> getProjectsByUserId(@Param("id") Long userId);
	
	@Query("select count(*) FROM Project pr where pr.user.id=:id")
	Long countProjectsOfUserId(@Param("id") Long userId);
}
