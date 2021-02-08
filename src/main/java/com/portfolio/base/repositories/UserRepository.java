package com.portfolio.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portfolio.base.models.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findUserByUserName(String userName);
	
	Boolean existsByUserName(String userName);
	
	User findUserById(Long id);
	
	@Query("SELECT count(*) FROM User user where user.id=:id and user.userName=:userName")
	Long checkUserExistsByUserNameAndUserId(String userName, Long id);
}
