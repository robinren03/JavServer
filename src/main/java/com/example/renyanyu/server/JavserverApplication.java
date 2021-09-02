package com.example.renyanyu.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import joinery.DataFrame;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class JavserverApplication {
	
	@RequestMapping("/")
    public String hello(){
        return "This is server of JavEduHw";
    }
	
	public static void main(String[] args) {
		SpringApplication.run(JavserverApplication.class, args);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> getList(
			@RequestParam(value = "course", required = true) String course)
	{
		Resource resource = new ClassPathResource(course+".csv");
		try {
			InputStream is = resource.getInputStream();
			DataFrame<Object> dt = DataFrame.readCsv(is);
			return dt.col(3);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}
}
