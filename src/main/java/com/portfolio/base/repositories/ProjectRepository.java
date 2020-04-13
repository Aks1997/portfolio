package com.portfolio.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.base.models.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{

}
