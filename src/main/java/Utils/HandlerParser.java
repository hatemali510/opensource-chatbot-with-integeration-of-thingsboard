package Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicBooleans;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.websocketdemo.WebsocketDemoApplication;
import com.example.websocketdemo.model.Telemetry;
@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class HandlerParser {
	
	
	public String convertTeletryModel(String jsonString) throws JSONException {
		String tel="";
		Long maxTs=Long.parseLong("0");
		while(jsonString.length()>0) {
			String test=jsonString.substring(1, jsonString.indexOf("]")+1);
			jsonString=jsonString.substring(jsonString.indexOf("]")+1);
			String telemtryname="";
			String telemteryvalue="";
			for(int i=0;i<test.length();i++) {
				if(test.charAt(i)==':') {
					for(int j=0;j<i;j++) {
						telemtryname+=test.charAt(j);
					}
					for(int j=i+1;j<test.length();j++) {
						telemteryvalue+=test.charAt(j);
					}
					break;
				}
			}
			telemtryname=telemtryname.replaceAll("\"", "");
			telemteryvalue=telemteryvalue.substring(1, telemteryvalue.length()-1);
			JSONObject json=new JSONObject(telemteryvalue);
			String ts=json.get("ts").toString();
			String value=json.get("value").toString();
			Calendar calendar = Calendar.getInstance();
			Long TS=Long.parseLong(ts);
			if(TS>=maxTs) {
				maxTs=TS;
				calendar.setTimeInMillis(TS);
				int hourse = calendar.get(Calendar.HOUR);
				int mins=calendar.get(calendar.MINUTE);
				int seconds=calendar.get(calendar.SECOND);
				String time=hourse+":"+mins+":"+seconds;
				String tel_="telemetry name : ("+telemtryname + ") in  "+ time ;
				tel+=" , "+tel_;
			}
			if(jsonString.length()<=1) {
				if(tel.length()>0) {
				return tel;
				}else {
					tel="no telemetry found ! ";
					return tel;
				}
				
			}
					
		}
		return "";
		 
		
	}
	
	public String convertMillToDate(String millSec) {
		Calendar calendar = Calendar.getInstance();
		Long TS=Long.parseLong(millSec);
		calendar.setTimeInMillis(TS);
		int hourse = calendar.get(Calendar.HOUR);
		int mins=calendar.get(calendar.MINUTE);
		int seconds=calendar.get(calendar.SECOND);
		String time=hourse+":"+mins+":"+seconds;
		return time;
	}
	

}
