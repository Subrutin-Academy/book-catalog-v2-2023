package com.subrutin.catalog.security.model;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class RawAccessJwtToken implements Token{
	
	private String token;
	

	public RawAccessJwtToken(String token) {
		super();
		this.token = token;
	}

	public Jws<Claims> parseClaims(Key signingKey) {
		return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
	}


	@Override
	public String getToken() {
		return this.token;
	}

}
