package com.krkn.movie.msvc.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.krkn.movie.msvc.db.DbRating;
import com.krkn.movie.msvc.pojo.VideoTitleYear;

public interface IRatingService {

	DbRating getRatingByURL(String url) throws IOException;

	DbRating getRatingByTitleYear(String title, String year, String url)
			throws JsonMappingException, JsonProcessingException;

	VideoTitleYear getTitleYrByUrl(String url) throws IOException;

}