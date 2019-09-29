package com.example.websocketdemo;

import java.io.File;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketDemoApplication {
	 private static final boolean TRACE_MODE = false;
	    static String botName = "super";
	public static void main(String[] args) {
		SpringApplication.run(WebsocketDemoApplication.class, args);
	}
	
}
