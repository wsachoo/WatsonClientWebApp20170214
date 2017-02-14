package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dataobject.PrintDO;
import com.example.db.DBService;

@Controller
public class Greeting {

	@Autowired
	private DBService objDBService;

	@RequestMapping("/greeting")
	@ResponseBody
	public PrintDO greeting() {
		PrintDO obj = objDBService.insertAndDisplaySampleData();
		return obj;
	}
}
