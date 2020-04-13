package com.portfolio.base.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordManager {

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public String encodeCredP(String credP) {
		return encoder.encode(credP);
	}
	
	public boolean compareCredP(String credP, String hash) {
		return encoder.matches(credP, hash);
	}

}
