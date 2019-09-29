package com.example.websocketdemo.integeration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.opencsv.CSVWriter;

import Utils.HandlerParser;
@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class DeviceIntegeration {
	private String deviceLimitNumber="1000";
	private String file_name="attributes_file";
	public HandlerParser parser=new HandlerParser();
	
	public ResponseEntity<String>  getAllDevices(String token) {
		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	//String url="http://"+platformConfig.getIp()+":"+platformConfig.getPort()+"/api/auth/user";
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
            
        	return result;
		} catch (HTTPException e) {
             e.printStackTrace();
             return null;
		}
	}
	public  String getLastTelemetry(String deviceId, String token) throws JSONException {
		//http://localhost:8080/api/plugins/telemetry/DEVICE/664bb450-d854-11e8-8c98-d7274a5fe3c1/values/timeseries
		String status="unknown";
		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	String url="http://localhost:8080/api/plugins/telemetry/DEVICE/"+deviceId+"/values/timeseries";
    			
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
        	 JSONObject jsonObject=new JSONObject();
        	 jsonObject.put("type", "telemetry");
             if(result.getBody().length()>2) {
            	 HandlerParser handlerParser=new HandlerParser();
            	 System.out.println(handlerParser.convertTeletryModel(result.getBody().toString()));
            	 jsonObject.put("message", handlerParser.convertTeletryModel(result.getBody().toString()));
             }else {
            	 jsonObject.put("message","no telemetry data has found");
             }
        	 return jsonObject.toString();
		} catch (HTTPException e) {
			e.printStackTrace();
             return "";
		}
	}



	public String getStatus(String DeviceId,String token) throws InterruptedException, JSONException {
		String status="unknown";
		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	String url="http://localhost:8080/api/plugins/telemetry/DEVICE/"+DeviceId+"/values/attributes";
    			
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
             
             JSONArray newJson=new JSONArray(result.getBody());
             for(int i=0;i<newJson.length();i++) {
            	 JSONObject obj = newJson.getJSONObject(i);
            	 if(obj.get("key").equals("active")) {
                	 JSONObject jsonObject=new JSONObject();
                	 jsonObject.put("type", "status");
            		 //System.out.println(obj.get("value"));
            		 String activeValue = obj.get("value").toString();
            		 if(activeValue.equals("false")) {
                    	 status="not Active";
                     }else {
                    	 status="active";
                     }
                	 jsonObject.put("message", status);

            		 return jsonObject.toString();
            	 }
             }
        	return status;
		} catch (HTTPException e) {
             return status;
		}
		
		
	}
	public String  getAll(String token) throws InterruptedException, JSONException{
		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	//String url="http://"+platformConfig.getIp()+":"+platformConfig.getPort()+"/api/auth/user";
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
             String result2="devices : ";
             for (int i = 0 ; i < newJson.length(); i++) {
                 JSONObject obj = newJson.getJSONObject(i);
                 String deviceName =  obj.get("name").toString();
                 result2+=deviceName +" -- ";
             }
        	 
        	return result2;
		} catch (HTTPException e) {
             e.printStackTrace();
             return "";
		}
	}
	
	public String getLastAlarm(String deviceId, String token) throws JSONException {
		//http://localhost:8080/api/alarm/DEVICE/664bb450-d854-11e8-8c98-d7274a5fe3c1?limit=1000&ascOrder=true

		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	String url="http://localhost:8080/api/alarm/DEVICE/"+deviceId+"?limit=1000&ascOrder=true";
    			
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
            JSONObject jsonObject=new JSONObject();

    		HttpEntity <String> httpEntity = new HttpEntity <String> (headers);
             result = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
             //System.out.println(result);
             JSONObject obj = new JSONObject(result.getBody()); 
             JSONArray alarms = obj.getJSONArray("data");
             jsonObject.put("type", "");
             if(alarms.length()>0) {
            	 jsonObject.put("message", alarms.getJSONObject(alarms.length()-1).toString());
            	 return jsonObject.toString();
             }
        	 jsonObject.put("message", "no alarms founded ! ");
        	 
             return jsonObject.toString() ;
		} catch (HTTPException e) {
             return "";
		}
	}

	public ResponseEntity getFile(String file) throws IOException{
	    ResponseEntity respEntity = null;
	    //byte[] reportBytes = null;
	    File result=new File(file);
	    if(result.exists()){
	    	String path=result.getAbsolutePath();
	        InputStream inputStream = new FileInputStream(path);
	        //String type=result.toURL().openConnection().guessContentTypeFromName("accessToken.csv");

	        byte[]out=org.apache.commons.io.IOUtils.toByteArray(inputStream);

	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.add("content-disposition", "attachment; filename=" + file);
	        responseHeaders.add("Content-Type","text/csv");

	        respEntity = new ResponseEntity(out, responseHeaders,HttpStatus.OK);
	    }else{
	        respEntity = new ResponseEntity ("File Not Found", HttpStatus.OK);
	    }
	    System.out.println(respEntity);
	    return respEntity;
	}
	
	public String getDeviceAttributes(String DeviceId,String token,String deviceName) throws InterruptedException, JSONException, IOException {
		ArrayList<ArrayList<String>> csvFileData=new ArrayList<>();

		String status="unknown";
		URI uri = null;
		ResponseEntity<String> result=null;
    	RestTemplate restTemplate=new RestTemplate();
    	String url="http://localhost:8080/api/plugins/telemetry/DEVICE/"+DeviceId+"/values/attributes";
    			
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
             System.out.println(result.getBody());
             JSONArray newJson=new JSONArray(result.getBody());
             String attributes="";
             for(int i=0;i<newJson.length();i++) {
            	JSONObject jsonObject=newJson.getJSONObject(i);
            	if(jsonObject.getString("key").equals("inactivityAlarmTime")||jsonObject.getString("key").equals("lastActivityTime")) {
	            	String time=parser.convertMillToDate(jsonObject.getString("value"));
	            	attributes+="( key : "+jsonObject.getString("key") +" , "+ "value : "+time+" ) ,"+"\n";
					ArrayList<String> row=new ArrayList<>();
	            	row.add(jsonObject.getString("key")); row.add(time); 
					csvFileData.add(row);
            	}else {
            		attributes+="( key : "+jsonObject.getString("key") +" , "+ "value : "+jsonObject.getString("value")+" ) ,"+"\n";
    				ArrayList<String> row=new ArrayList<>();
                	row.add(jsonObject.getString("key")); row.add(jsonObject.getString("value")); 
    				csvFileData.add(row);
            	}
             }
             JSONObject jsonObject=new JSONObject();
             jsonObject.put("type", "attributes");
             jsonObject.put("message", attributes);
             CSVWriter csvWriter = null;
     		try {
     			file_name=file_name+"_"+deviceName+".csv";
     			csvWriter = new CSVWriter(new FileWriter(file_name));
     			csvWriter.writeNext(new String[] {"key","value"});
     			for(int i=0;i<csvFileData.size();i++) {
     				ArrayList<String> row=csvFileData.get(i);
     				String [] data= {row.get(0),row.get(1)};
     				
     			        csvWriter.writeNext(data);
     			}
     			csvWriter.close();
                jsonObject.put("fileName",file_name );
                //getFile(file_name);

     		} catch (IOException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
        	 return jsonObject.toString();
		} catch (HTTPException e) {
             return status;
		}
		
		
	}
	

}
