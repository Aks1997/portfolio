package com.portfolio.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.base.models.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findUserByUserName(String userName);
	
	Boolean existsByUserName(String userName);
}
