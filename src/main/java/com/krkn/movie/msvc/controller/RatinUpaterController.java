package com.krkn.movie.msvc.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krkn.movie.msvc.service.RatingUpdaterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/video")
public class RatinUpaterController {

	
	@Autowired
	RatingUpdaterService ratingUpdaterService;

	@Scheduled(cron = "0 0 23 * * *")
	@GetMapping(path = "/v1/video/sch")
	public void runsch() throws InterruptedException, IOException {
		ratingUpdaterService.updateRatings();

	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleException(RuntimeException e) {
		log.error("inside UserController : handleException {}", e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

}
