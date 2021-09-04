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
import joinery.DataFrame.Predicate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
			@RequestParam(value = "course", required = true) String course,
			@RequestParam(value = "page", required = true)int page)
	{
		Resource resource = new ClassPathResource("./"+ course + ".csv");
		try {
			InputStream is = resource.getInputStream();
			DataFrame<Object> dt = DataFrame.readCsv(is);
			List<Object> ls = dt.select(new Predicate<Object>() {
				@Override
				public Boolean apply(List<Object> values) {
					return String.class.cast(values.get(1)).equals("http://www.w3.org/2000/01/rdf-schema#label");
				}
			}).slice(page*15, page*15 + 14).col(0);
			return ls;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}
}
