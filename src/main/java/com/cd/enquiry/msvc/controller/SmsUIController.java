package com.cd.enquiry.msvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SmsUIController {
	
	@GetMapping("/v1/sendMessages")
	public String hello() {
		log.info("sendMessages method called :");
		return "sendMessages";
	}

	@GetMapping("/v1/getReport")
	public String getReport() {
		log.info("getReport method called :");
		return "sendExcelMail";
	}


}
