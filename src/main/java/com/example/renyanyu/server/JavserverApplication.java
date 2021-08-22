package com.example.renyanyu.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class JavserverApplication {
	
	@RequestMapping("/")
    public String hello(){
        return "This is the server of JavEduHw";
    }
	
	public static void main(String[] args) {
		SpringApplication.run(JavserverApplication.class, args);
	}

}
