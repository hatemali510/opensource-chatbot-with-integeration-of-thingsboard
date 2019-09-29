package com.example.websocketdemo.integeration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.ws.http.HTTPException;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;



@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class chatBotIntegreation {
	 private static String deviceLimitNumber="1000";
	 boolean helloMessage=true;
   
	
	
	DeviceIntegeration deviceIntegeration=new DeviceIntegeration();

	private static final boolean TRACE_MODE = false;
	static String botName = "super";
	
    public  String  chat(String message, String token,String machineName) throws JSONException {
		System.out.println(machineName);
		JSONObject messageObject=new JSONObject(message);
		String senderMessage=messageObject.get("content").toString();
		 String resourcesPath = getResourcesPath();
         MagicBooleans.trace_mode = TRACE_MODE;
         Bot bot = new Bot("super", resourcesPath);
         Chat chatSession = new Chat(bot);
         bot.brain.nodeStats();
		if(!machineName.equals("Robot")) {
            String response=response(senderMessage, chatSession);
			try {
				String result=DeviceCase(machineName, token, senderMessage);
				
				if(result.equals("no result found !")) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("type", "device");
					jsonObject.put("message",response );
					return jsonObject.toString();
				}else {
					return result;
				}
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("type", "device");
				jsonObject.put("message","no operation has found !" );

				return jsonObject.toString();
			}
		}else {
			try {
				JSONObject jsonObject=new JSONObject();

	            String response=response(senderMessage, chatSession);
		            if(response.equals("devices")) {
		            	String devices= deviceIntegeration.getAll(token);
		            	devices=devices.substring(0, devices.length() - 3);
		            	jsonObject.put("type", "robot");
		            	jsonObject.put("message", devices);
		            	return jsonObject.toString();
		            }else {
		            	System.out.println("robot case: "+machineName);
		    			String robotResult=RobotCase(senderMessage, token);
		    			if(!robotResult.equals("no case")) {
		    				return robotResult;
		    			}else {
		    				jsonObject.put("type", "chatbot");
		    				jsonObject.put("message", response);
		    				return jsonObject.toString();
		    			}
		            }
			} catch (Exception e) {
					e.printStackTrace();
			}
		}
		return null;
	}
	
	
	private String Operations(String devicename,String token,int operation) throws JSONException, InterruptedException, IOException {
		String status;
		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
		String url="http://localhost:8080/api/tenant/devices?limit="+deviceLimitNumber;
    	try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	HttpHeaders headers = new HttpHeaders();
    	headers.clear();
    	headers.add("X-Authorization","Bearer "+token);
    	//System.out.println(headers);
    	try {
    		HttpEntity <String> httpEntity = new HttpEntity <String> (headers);
             result = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
             JSONObject jsnobject = new JSONObject(result.getBody());
             JSONArray newJson=(JSONArray)jsnobject.getJSONArray("data");
             for (int i = 0 ; i < newJson.length(); i++) {
                 JSONObject obj = newJson.getJSONObject(i);
                 String name =  obj.get("name").toString();
                 JSONObject id=(JSONObject) obj.get("id");
                 String DeviceId =id.get("id").toString();
                 
                 if(devicename.equals(name)) {
                	 if(operation==1) {
                	 status=deviceIntegeration.getStatus(DeviceId, token);
                	 return status;
                	 }if(operation==2){
                		 // get Device attributes
                		 String attributes =deviceIntegeration.getDeviceAttributes(DeviceId, token,devicename);
                		 return attributes;
                		 
                	 }if(operation==3) {
                		//  get device last telemetry
                		 String LastTelemtry=deviceIntegeration.getLastTelemetry(DeviceId,token);
                		 return LastTelemtry;
                	 }
                	 if (operation ==4) {
                		 // get device last alarm
                		 String LastAlarm=deviceIntegeration.getLastAlarm(DeviceId,token);
                		 return LastAlarm;
                		 
                	 }
                 }
                 
             }
             status="unknown";
        	return status;
		} catch (HTTPException e) {
             e.printStackTrace();
             status="unknown";
             return status;
		}
		
		
	}

	
    public String DeviceCase(String DeviceName,String token,String message) throws JSONException, InterruptedException, IOException {
		String request = message;
        if(request.contains("status") || request.contains("Status")|| request.contains("STATUS")) {
        	int operation =1;
        	System.out.println("----- status case -----");
        	System.out.println("----- with token ="+ token+ "-----");
        	String devices=deviceIntegeration.getAll(token);
        	devices=devices.substring(10,devices.length()-3);
        	//System.out.println(devices);
        	String[] Devices=devices.split("--");
        	for(int i=0;i<Devices.length;i++) {
        		Devices[i]=Devices[i].replaceAll(" ", "");
        	}
        	String Status = Operations(DeviceName, token, operation);
        	return Status; 
        }
        if(request.contains("attributes") || request.contains("Attributes") || request.contains("ATTRIBUTES")) {
        	int operation =2;

        	System.out.println("----- attributes case -----");
        	System.out.println("----- with token ="+ token+ "-----");
        	String devices=deviceIntegeration.getAll(token);
        	devices=devices.substring(10,devices.length()-3);
        	String[] Devices=devices.split("--");
        	for(int i=0;i<Devices.length;i++) {
        		Devices[i]=Devices[i].replaceAll(" ", "");
        	}
        	String Attributes = Operations(DeviceName, token,operation);
        	return Attributes; 
        }
        if(request.contains("telemetry") || request.contains("TELEMETRT") || request.contains("Telemetry")) {
        	int operation =3;

        	System.out.println("----- telemetry case -----");
        	System.out.println("----- with token ="+ token+ "-----");
        	String devices=deviceIntegeration.getAll(token);
        	devices=devices.substring(10,devices.length()-3);
        	String[] Devices=devices.split("--");
        	for(int i=0;i<Devices.length;i++) {
        		Devices[i]=Devices[i].replaceAll(" ", "");
        	}
        	
        	String telemetry = Operations(DeviceName, token,operation);
        	return telemetry; 
        }
        if(request.contains("alarm") || request.contains("ALARM") || request.contains("Alarm")) {
        	int operation =4;

        	System.out.println("----- alarm case -----");
        	System.out.println("----- with token ="+ token+ "-----");
        	String devices=deviceIntegeration.getAll(token);
        	devices=devices.substring(10,devices.length()-3);
        	String[] Devices=devices.split("--");
        	for(int i=0;i<Devices.length;i++) {
        		Devices[i]=Devices[i].replaceAll(" ", "");
        	}
        	
        	String alarm = Operations(DeviceName, token,operation);
        	return alarm; 
        }
        
        return "no result found !";
	}
	
	
	public String RobotCase(String message,String token) throws InterruptedException, JSONException, IOException {
		String request = message;
        if(request.contains("status") || request.contains("Status")|| request.contains("STATUS")) {
        	int operation =1;
        	System.out.println("----- status case -----");
        	System.out.println("----- with token ="+ token+ "-----");
        	String[] sentence=request.split(" ");
        	String devices=deviceIntegeration.getAll(token);
        	devices=devices.substring(10,devices.length()-3);
        	//System.out.println(devices);
        	String[] Devices=devices.split("--");
        	for(int i=0;i<Devices.length;i++) {
        		Devices[i]=Devices[i].replaceAll(" ", "");
        	}
        	for(int i=0;i<sentence.length;i++) {
        		for(int j=0;j<Devices.length;j++) {
        			if(sentence[i].equals(Devices[j])) {
        				String Status = Operations(Devices[j], token, operation);
                    	return Status; 
        			}
        		}
        	}
        }
        if(request.contains("attributes") || request.contains("Attributes") || request.contains("ATTRIBUTES")) {
        	int operation =2;

        	System.out.println("----- attributes case -----");
        	System.out.println("----- with token ="+ token+ "-----");
        	String[] sentence=request.split(" ");
        	String devices=deviceIntegeration.getAll(token);
        	devices=devices.substring(10,devices.length()-3);
        	String[] Devices=devices.split("--");
        	for(int i=0;i<Devices.length;i++) {
        		Devices[i]=Devices[i].replaceAll(" ", "");
        	}
        	
        	for(int i=0;i<sentence.length;i++) {
        		for(int j=0;j<Devices.length;j++) {
        			if(sentence[i].equals(Devices[j])) {
        				String Attributes = Operations(Devices[j], token,operation);
                    	return Attributes ;
        			}
        		}
        	}
        }
        if(request.contains("telemetry") || request.contains("TELEMETRT") || request.contains("Telemetry")) {
        	int operation =3;

        	System.out.println("----- telemetry case -----");
        	System.out.println("----- with token ="+ token+ "-----");
        	String[] sentence=request.split(" ");
        	String devices=deviceIntegeration.getAll(token);
        	devices=devices.substring(10,devices.length()-3);
        	String[] Devices=devices.split("--");
        	for(int i=0;i<Devices.length;i++) {
        		Devices[i]=Devices[i].replaceAll(" ", "");
        	}
        	
        	for(int i=0;i<sentence.length;i++) {
        		for(int j=0;j<Devices.length;j++) {
        			if(sentence[i].equals(Devices[j])) {
        				String telemetry = Operations(Devices[j], token,operation);
                    	return telemetry; 
        			}
        		}
        	}
        }
        if(request.contains("alarm") || request.contains("ALARM") || request.contains("Alarm")) {
        	int operation =4;

        	System.out.println("----- alarm case -----");
        	System.out.println("----- with token ="+ token+ "-----");
        	String[] sentence=request.split(" ");
        	String devices=deviceIntegeration.getAll(token);
        	devices=devices.substring(10,devices.length()-3);
        	String[] Devices=devices.split("--");
        	for(int i=0;i<Devices.length;i++) {
        		Devices[i]=Devices[i].replaceAll(" ", "");
        	}
        	
        	for(int i=0;i<sentence.length;i++) {
        		for(int j=0;j<Devices.length;j++) {
        			if(sentence[i].equals(Devices[j])) {
        				String alarm = Operations(Devices[j], token,operation);
                    	return alarm; 
        			}
        		}
        	}
        }
        return "no case";
	}
	
	
	private  String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }

	
	public String response(String request,Chat chatSession) {
		
		String response = chatSession.multisentenceRespond(request);
        while (response.contains("&lt;"))
        response = response.replace("&lt;", "<");
        while (response.contains("&gt;"))
        response = response.replace("&gt;", ">");
        
        return response;
	}

	
}




