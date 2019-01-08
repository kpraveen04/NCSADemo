package com.javasampleapproach.mysql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.mysql.model.Customer;
import com.javasampleapproach.mysql.repo.CustomerRepository;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;


@RestController
public class WebController {
	@Autowired
	CustomerRepository repository;
	
	@RequestMapping("/save")
	public String process(){
		repository.save(new Customer("Jack", "Smith"));
		repository.save(new Customer("Adam", "Johnson"));
		repository.save(new Customer("Kim", "Smith"));
		repository.save(new Customer("David", "Williams"));
		repository.save(new Customer("Peter", "Davis"));
		return "Done";
	}
	
	
	@RequestMapping("/findall")
	public String findAll(){
		String result = "<html>";
		
		for(Customer cust : repository.findAll()){
			result += "<div>" + cust.toString() + "</div>";
		}
		
		return result + "</html>";
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/findbyid")
	public String findById(@RequestBody long id){
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/findbylastname")
	public String fetchDataByLastName(@RequestBody String lastName){
		String result = "<html>";
		String name ="";
		JSONParser parser = new JSONParser();
		Object resultObject;
		try {
			resultObject = parser.parse(lastName);
			JSONObject obj = (JSONObject) resultObject;
			name =(String) obj.get("lastname");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(Customer cust: repository.findByLastName(lastName)){
			result += "<div>" + cust.toString() + "</div>"; 
		}
		
		return result + "</html>";
	}
}