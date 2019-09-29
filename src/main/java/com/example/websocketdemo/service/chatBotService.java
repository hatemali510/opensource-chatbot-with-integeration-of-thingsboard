package com.example.websocketdemo.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.websocketdemo.integeration.DeviceIntegeration;
import com.example.websocketdemo.integeration.chatBotIntegreation;
@Component
public class chatBotService {
	
	public String chat(String message,String token,String machineName) throws JSONException {
		chatBotIntegreation chatBotIntegreation=new chatBotIntegreation();
		return chatBotIntegreation.chat(message,token,machineName); 
	}

	public ResponseEntity<String> getAllDevices(String token) {
		DeviceIntegeration deviceIntegeration=new DeviceIntegeration();
		return deviceIntegeration.getAllDevices(token);
	}

	
	
	

}
