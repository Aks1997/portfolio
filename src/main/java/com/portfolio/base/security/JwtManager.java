package com.portfolio.base.security;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.portfolio.base.common.CommonConstants;

@Component
public class JwtManager {

	@Value("${app.jwt.secret}")
	private String secretKey;
	
	private Algorithm algorithm;
	
	private JWTVerifier verifier;
	
	private Algorithm getAlgorithm() {
		if(algorithm==null) {
			algorithm= Algorithm.HMAC256(secretKey);
		}
		return algorithm;
	}
	
	private JWTVerifier getJwtVerifier() {
		if(verifier==null) {
			verifier= JWT.require(getAlgorithm())
			        .build();
		}
		return verifier;
	}
	
	public String createJwt(String userName) {
		//HMAC
		Map<String, Object> data= new HashMap<String, Object>();
		data.put(CommonConstants.USER_NAME, userName);
		
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, 4);
		Date expirationDate = cal.getTime();
		
		String token;
		try {
			token= JWT.create()
					.withClaim(CommonConstants.USER_NAME, userName)
					.withIssuedAt(currentDate)
					.withExpiresAt(expirationDate)
					.sign(getAlgorithm());
		}catch(JWTCreationException exception) {
			return null;
		}
		return token;
	}
	
	public String verifyJwt(String token) {
		try {
			DecodedJWT jwt = getJwtVerifier().verify(token);
			return jwt.getClaim(CommonConstants.USER_NAME).asString();
		} catch (JWTVerificationException exception){
		    //Invalid signature/claims/expired
			return null;
		}
	}
	
	public String getUserNameFromJwt(String token) {
		return verifyJwt(token);
	}
}
