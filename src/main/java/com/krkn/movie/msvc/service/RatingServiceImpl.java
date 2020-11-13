package com.krkn.movie.msvc.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.krkn.movie.msvc.config.DbChannel;
import com.krkn.movie.msvc.constants.Constants;
import com.krkn.movie.msvc.db.DbRating;
import com.krkn.movie.msvc.pojo.VideoTitleYear;
import com.krkn.movie.msvc.util.ExtractTitleYearUtil;
import com.krkn.movie.msvc.util.SaveVideoTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RatingServiceImpl implements Constants, IRatingService {

	@Autowired
	DbChannel dbChannel;

	@Autowired
	OmdbService omdbService;

	@Override
	public DbRating getRatingByURL(String url) throws IOException {
		DbRating dbRating;
		dbRating = dbChannel.getRatingByUrl(url);
		if (dbRating == null) {
			VideoTitleYear titleYr = getTitleYrByUrl(url);
			dbRating = getRatingByTitleYear(titleYr.getTitle(), titleYr.getYear(), url);
		}
		return dbRating;

	}

	@Override
	public DbRating getRatingByTitleYear(String title, String year, String url)
			throws JsonMappingException, JsonProcessingException {
		title = validateTitle(title);

		DbRating dbVideo = dbChannel.getRatingByTitleYr(title, year);

		if (dbVideo == null) {
			String response = omdbService.getOmdbResponse(title, year);
			dbVideo = omdbService.omdbResponseToDbVideoMapper(response, url);
		}
		SaveVideoTask task = new SaveVideoTask(dbVideo, dbChannel);
		task.start();
		return dbVideo;
	}


	@Override
	public VideoTitleYear getTitleYrByUrl(String url) throws IOException {
		VideoTitleYear vty = ExtractTitleYearUtil.getTitleYear(url);
		log.info("extracted title :" + vty.getTitle() + vty.getYear() + " from url :" + url);
		return vty;
	}
	
	private String validateTitle(String title) {
		if (title == null || !(title.length() > 0))
			throw new RuntimeException("title is blank");
		if (title.contains("http"))
			throw new RuntimeException("invalid title");
		title = title.trim();
		return title;
	}


}
