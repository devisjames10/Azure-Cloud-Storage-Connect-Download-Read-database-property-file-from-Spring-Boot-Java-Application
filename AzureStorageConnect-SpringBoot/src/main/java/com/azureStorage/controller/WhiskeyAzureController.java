package com.azureStorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WhiskeyAzureController {

	 @Autowired
	    private JdbcTemplate jdbcTemplate;
	 
	 @RequestMapping(value="/testing/{emp_id}", method= RequestMethod.GET)
	 public String storageTest(@PathVariable("emp_id") int emp_id) {
		
		System.out.println("==========  starting: storageTest  ===========");
				
		//int empId=emp_id;
		String query = "select name from employee where emp_id=?"; 
		 Object[] inputs = new Object[] {emp_id};
		 String empName = jdbcTemplate.queryForObject(query, inputs, String.class);
		
		System.out.println(" empName =======>> "+empName);
		 
		return "========== storageTest ===========  " +empName+ "  ==================";
		
			
	}
	
	
}
