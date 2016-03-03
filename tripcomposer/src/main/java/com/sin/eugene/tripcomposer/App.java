package com.sin.eugene.tripcomposer;

import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sin.eugene.tripcomposer.dao.Country;
import com.sin.eugene.tripcomposer.service.CountryService;

public class App {

	public static void main(String[] args) {

		start();

	}
	
	private static void start(){

		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring.cfg.xml");
		CountryService service = context.getBean(CountryService.class);
		
		service.addAllFromRemote();

		List<Country> list = service.selectAllCountries();
		list.forEach(country -> {
			System.out.println(country);
			System.out.println(country.citiesToString());
		});
		
		context.close();

	}
	
}

