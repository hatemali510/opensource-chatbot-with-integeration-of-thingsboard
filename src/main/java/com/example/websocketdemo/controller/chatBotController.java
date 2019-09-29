package com.example.websocketdemo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.websocketdemo.integeration.DeviceIntegeration;
import com.example.websocketdemo.model.User;
import com.example.websocketdemo.service.chatBotService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class chatBotController {
	@Autowired
	chatBotService chatBotService;
	@Autowired
	DeviceIntegeration deviceIntegeration;
	String file_name="attributes_file.csv";
	@RequestMapping(value="/chatRobot", method = RequestMethod.POST)
    public String  getUserData(@RequestBody String message,@RequestHeader String token,@RequestHeader String machineName) throws JSONException {
		String result=chatBotService.chat(message,token,machineName);
		System.out.println(result);
    	return result; 
		
    }
	
	@RequestMapping(value="/getallDevices", method = RequestMethod.GET)
    public ResponseEntity<String>  getUserData(@RequestHeader String token)  {
		//System.out.println(token);
    	return chatBotService.getAllDevices(token); 
		
    }
	
	
	@RequestMapping(value="/getFile", method = RequestMethod.GET)
    public ResponseEntity  getFile(@RequestHeader String fileName) throws IOException  {
		System.out.println(fileName);
    	return deviceIntegeration.getFile(fileName); 
    }
	
	
	
	
	
	

}
