package com.krkn.movie.msvc.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krkn.movie.msvc.db.DbRating;
import com.krkn.movie.msvc.pojo.VideoTitleYear;
import com.krkn.movie.msvc.service.IRatingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/video")
public class RatingController {

	@Autowired
	IRatingService ratingService;

	@GetMapping(path = "/v1/rating/titleYr")
	public ResponseEntity<DbRating> getRatingByTitleYear(@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "year", required = false) String year) throws InterruptedException, IOException {
		log.debug("getRatingByTitleYear called :{} {} ", title, year);
		DbRating rating = ratingService.getRatingByTitleYear(title, year, "");
		log.debug("getRatingByTitleYear finished ");
		return ResponseEntity.ok(rating);
	}

	@GetMapping(path = "/v1/rating/url")
	public ResponseEntity<DbRating> getRatingByURL(@RequestParam(value = "url", required = true) String url)
			throws InterruptedException, IOException {
		log.debug("getRatingByURL called : ", url);
		DbRating rating = ratingService.getRatingByURL(url);
		log.debug("getRatingByURL finished ");
		return ResponseEntity.ok(rating);
	}

	@GetMapping(path = "/v1/title-yr/url")
	public ResponseEntity<VideoTitleYear> getTitleYearByUrl(@RequestParam(value = "url", required = true) String url)
			throws InterruptedException, IOException {
		log.debug("getTitleYearByUrl called : ", url);
		VideoTitleYear titleYr = ratingService.getTitleYrByUrl(url);
		log.debug("getTitleYearByUrl finished ");
		return ResponseEntity.ok(titleYr);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleException(RuntimeException e) {
		log.error("inside UserController : handleException {}", e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

}
