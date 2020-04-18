package com.portfolio.base.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordManager {
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	public String encodeCredP(String credP) {
		return encoder.encode(credP);
	}
	
	public boolean compareCredP(String credP, String hash) {
		return encoder.matches(credP, hash);
	}

}
