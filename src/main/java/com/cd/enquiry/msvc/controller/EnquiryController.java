package com.cd.enquiry.msvc.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cd.enquiry.msvc.constants.CDConstants;
import com.cd.enquiry.msvc.requestdto.EnquiryDto;
import com.cd.enquiry.msvc.service.EnquiryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/enquiry")
public class EnquiryController {

	@Autowired
	EnquiryService enquiryService;

	@CrossOrigin
	@PostMapping(path = "/v1/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> admissionEnquiry(@RequestBody EnquiryDto enquiryDto ) {
		log.debug("admissionEnquiry called by EnquiryDto{} :", enquiryDto.toString());
		enquiryService.saveEnquiry(enquiryDto.getName(), enquiryDto.getEmail(), enquiryDto.getContact(),
				enquiryDto.getInterest());
		log.debug("admissionEnquiry finished by name{}, contact{}", enquiryDto.getName(), enquiryDto.getContact());
		return ResponseEntity.ok("enquiry submitted successfully:");
	}

	@GetMapping(path = "/v1/mail/interest")
	public String getMailForInterestedCandidates(
			@RequestParam(value = "numOfDaysToConsider", required = true) String numOfDaysToConsider)
			throws InterruptedException, IOException {
		log.debug("getMailForInterestedCandidates called ");
		Long days = Long.valueOf(numOfDaysToConsider);
		LocalDateTime date = null;
		if (days != 0)
			date = LocalDateTime.now().minusDays(days);
		else
			date = CDConstants.START_DATE;
		enquiryService.sendMailForInterestedCandidates(date);
		log.debug("sendMailForInterestedCandidates finished ");
		return "mail sent successfully:";
	}

}
