package com.krkn.movie.msvc.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krkn.movie.msvc.db.Video;
import com.krkn.movie.msvc.service.VideoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/video")
public class VideoController {

	@Autowired
	VideoService videoService;

	@PostMapping(path = "/v1/video/create")
	public String createVideoFromTsv(@RequestParam(value = "path", required = true) String path)
			throws InterruptedException, IOException {
		log.debug("createVideoFromTsv called : ", path);
		videoService.createVideoFromTsv(path);
		log.debug("getVideoByTitle finished ");
		return "video saved successfully:";
	}

	@PostMapping(path = "/v1/add/rating")
	public String addRatingToVideos(@RequestParam(value = "path", required = true) String path)
			throws InterruptedException, IOException {
		log.debug("createVideoFromTsv called : ", path);
		videoService.addImdbRatings(path);
		log.debug("addRatingToVideos finished ");
		return "video rating saved successfully:";
	}

	@GetMapping(path = "/v1/video/title")
	public ResponseEntity<Video> getVideoByTitle(@RequestParam(value = "title", required = true) String title)
			throws InterruptedException, IOException {
		log.debug("getVideoByTitle called : ", title);
		Video video = videoService.getVideoByTitle(title);
		log.debug("getVideoByTitle finished ");
		return ResponseEntity.ok(video);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleException(RuntimeException e) {
		log.error("inside UserController : handleException {}", e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

}
