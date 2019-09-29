package com.example.websocketdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.*;
import com.example.websocketdemo.integeration.AuthIntegrator;
import com.example.websocketdemo.model.User;;
@Component
public class AuthService {
	@Autowired
	public AuthIntegrator authIntegrator;
	
	
	public ResponseEntity<String> auth(User user) {
		return authIntegrator.auth(user);
	}


	public ResponseEntity<String> getUser(String token) {
		
		return authIntegrator.getUser(token);
	}
}
