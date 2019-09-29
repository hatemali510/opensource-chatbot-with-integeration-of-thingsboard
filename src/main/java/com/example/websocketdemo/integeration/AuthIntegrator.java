package com.example.websocketdemo.integeration;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.ws.http.HTTPException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.websocketdemo.config.PlatformConfiguration;
import com.example.websocketdemo.model.User;
@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class AuthIntegrator {
	 private static String deviceLimitNumber="1000";

    @Autowired
    public PlatformConfiguration platformConfig;
	public ResponseEntity<String> auth(User user) {
    	URI uri = null;
    	ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	String url="http://"+platformConfig.getIp()+":"+platformConfig.getPort()+"/api/auth/login";
    	System.out.println(url);
    	try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	HttpHeaders headers = new HttpHeaders();
    	headers.clear();
    	headers.add("Content-Type", "application/json");
    	String body = user.ConvertToJson(user);
    	try {
    		HttpEntity <String> httpEntity = new HttpEntity <String> (body, headers);
             result = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        	return result;
		} catch (Exception e) {
			String data="no user founded !";
			return ResponseEntity.notFound().build();
                    
		}
    }
	
	
			
	public ResponseEntity<String> AuthUser(String token) {
		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	String url="http://"+platformConfig.getIp()+":"+platformConfig.getPort()+"/api/auth/user";
    	try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	HttpHeaders headers = new HttpHeaders();
    	headers.clear();
    	headers.add("Content-Type", "application/json");
    	
    	try {
    		HttpEntity <String> httpEntity = new HttpEntity <String> ( headers);
            result = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        	return result;
		} catch (Exception e) {
			return result;
		}
		
	}
	public ResponseEntity<String> getUser(String token) {
		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	String url="http://"+platformConfig.getIp()+":"+platformConfig.getPort()+"/api/auth/user";
    	try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	HttpHeaders headers = new HttpHeaders();
    	headers.clear();
    	headers.add("X-Authorization","Bearer "+token);
    	
    	try {
    		HttpEntity <String> httpEntity = new HttpEntity <String> ( headers);
            result = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        	return result;
		} catch (Exception e) {
			return result;
		}
		
		
	}
	
}
