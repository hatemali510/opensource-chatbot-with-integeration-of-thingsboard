package com.example.websocketdemo.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.websocketdemo.model.User;
import com.example.websocketdemo.service.AuthService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {
	
	String UserToken=null;
	@Autowired
	public AuthService authService;
	
	@RequestMapping(value="/auth", method = RequestMethod.POST)
    public ResponseEntity<String>  getToken(@RequestBody User user) {
    	return authService.auth(user); 
    }
	@RequestMapping(value="/getUser", method = RequestMethod.GET)
    public ResponseEntity<String>  getUserData(@RequestHeader String token) {
    	return authService.getUser(token); 
    }
	
	
}
