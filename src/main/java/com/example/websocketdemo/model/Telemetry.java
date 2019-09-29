package com.example.websocketdemo.model;

public class Telemetry {
	
	private String name;
	private String time;
	private String value;
	
	
	public Telemetry(String telemtryname, String time, String value) {
		this.name=telemtryname;
		this.time=time;
		this.value=value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
