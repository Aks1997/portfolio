package com.portfolio.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.base.models.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

}
