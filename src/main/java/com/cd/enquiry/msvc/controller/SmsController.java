package com.cd.enquiry.msvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cd.enquiry.msvc.service.SmsService;
import com.cd.enquiry.msvc.util.ControllerUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/sms")
public class SmsController {

	@Autowired
	SmsService smsService;

	@PostMapping(path = "/v1/sendPromotional")
	public ResponseEntity<String> send(@RequestParam String passCode, @RequestParam MultipartFile file,
			@RequestParam(required = false, defaultValue = com.cd.enquiry.msvc.constants.CDConstants.MESSAGE) String message) {
		log.info("sendPromotional method called :");
		if (!passCode.equals("Aug@2018"))
			return null;
		return ResponseEntity.status(HttpStatus.OK).body(smsService.sendPromotional(file, message));
	}

	@CrossOrigin
	@PostMapping(path = "/v1/sendOtp")
	public ResponseEntity<String> sendOtp(@RequestParam String ph, HttpServletRequest request) {
		log.info("sendOtp method called :");
		String ip = ControllerUtility.getClientIpAddr(request);

		return ResponseEntity.status(HttpStatus.OK).body(smsService.sendOtp(ph, ip));
	}

	@CrossOrigin
	@PostMapping(path = "/v1/verifyOtp")
	public ResponseEntity<Boolean> verifyOtp(@RequestParam String ph, @RequestParam Integer otp)
			throws InterruptedException {
		log.info("verifyOtp method called :");
		return ResponseEntity.status(HttpStatus.OK).body(smsService.verifyOtp(ph, otp));
	}

	@DeleteMapping(path = "/v1/cleanCache")
	public ResponseEntity<String> cleanCache(@RequestParam String passCode) {
		log.info("cleanCache method called :");
		// TODO: encrypt pass code
		if (!passCode.equals("Aug@2018"))
			return null;
		return ResponseEntity.ok(smsService.cleanCache());
	}

	@GetMapping(path = "/v1/seeCacheValues")
	public ResponseEntity<String> getCache() {
		log.info("getCache method called :");
		return ResponseEntity.ok(smsService.getCache());
	}

}
