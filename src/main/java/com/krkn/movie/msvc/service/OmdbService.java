package com.krkn.movie.msvc.service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.krkn.movie.msvc.config.DbChannel;
import com.krkn.movie.msvc.constants.Constants;
import com.krkn.movie.msvc.db.DbRating;
import com.krkn.movie.msvc.db.Rating;
import com.krkn.movie.msvc.db.SourceType;
import com.krkn.movie.msvc.db.VideoType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OmdbService implements Constants {

	@Autowired
	DbChannel dbChannel;

	@Value("${omdb.url}")
	private String omdbUrl;

	@Value("${omdb.apikey}")
	private String omdbApiKey;

	public String getOmdbResponse(String title, String year) {
		log.info("calling omdb api with title: year: " + title + year);
		String url = omdbUrl + omdbApiKey + "&t=" + title + "&y=" + year;
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url, String.class);
		if (result.contains("Response\":\"False")) {
			url = omdbUrl + omdbApiKey + "&t=" + title;
			result = restTemplate.getForObject(url, String.class);
		}
		return result;
	}

	// TODO: handle exception
	public DbRating omdbResponseToDbVideoMapper(String response, String url)
			throws JsonMappingException, JsonProcessingException {
		DbRating dbVideo = new DbRating();
		JSONObject videoJson = new JSONObject(response);
		dbVideo.setUrl(url);
		dbVideo.setTitle(videoJson.getString("Title"));
		dbVideo.setAwards(videoJson.getString("Awards"));
		dbVideo.setActor(videoJson.getString("Actors"));
		dbVideo.setWriter(videoJson.getString("Writer"));
		dbVideo.setCountry(videoJson.getString("Country"));
		dbVideo.setDirector(videoJson.getString("Director"));
		dbVideo.setGenre(videoJson.getString("Genre"));
		dbVideo.setImdbID(videoJson.getString("imdbID"));

		dbVideo.setLanguage(videoJson.getString("Language"));
		dbVideo.setPlot(videoJson.getString("Plot"));
		dbVideo.setPoster(videoJson.getString("Poster"));
		dbVideo.setRated(videoJson.getString("Rated"));
		dbVideo.setReleasedDate(videoJson.getString("Released"));
		dbVideo.setRuntime(videoJson.getString("Runtime"));
		dbVideo.setYear(videoJson.getString("Year"));
		dbVideo.setImdbVotes(videoJson.getString("imdbVotes"));
		dbVideo.setDateUpdated(LocalDate.now());
		dbVideo.setType(VideoType.getVideoType(videoJson.getString("Type")));
		JSONArray omdbRating = videoJson.getJSONArray("Ratings");
		for (int i = 0; i < omdbRating.length(); ++i) {
			JSONObject omdbrat = omdbRating.getJSONObject(i);
			Rating dbrating = new Rating();
			dbrating.setSource(SourceType.getSourceType(omdbrat.getString("Source")));
			dbrating.setRating(omdbrat.getString("Value"));
			dbVideo.getRatings().add(dbrating);
		}
		return dbVideo;
	}

	public void updateRatings(String response, DbRating rating) {
		JSONObject videoJson = new JSONObject(response);

		rating.setTitle(videoJson.getString("Title"));
		rating.setAwards(videoJson.getString("Awards"));
		rating.setActor(videoJson.getString("Actors"));
		rating.setWriter(videoJson.getString("Writer"));
		rating.setCountry(videoJson.getString("Country"));
		rating.setDirector(videoJson.getString("Director"));
		rating.setGenre(videoJson.getString("Genre"));
		rating.setImdbID(videoJson.getString("imdbID"));
		rating.setLanguage(videoJson.getString("Language"));
		rating.setPlot(videoJson.getString("Plot"));
		rating.setPoster(videoJson.getString("Poster"));
		rating.setRated(videoJson.getString("Rated"));
		rating.setReleasedDate(videoJson.getString("Released"));
		rating.setRuntime(videoJson.getString("Runtime"));
		rating.setYear(videoJson.getString("Year"));
		rating.setImdbVotes(videoJson.getString("imdbVotes"));
		rating.setDateUpdated(LocalDate.now());
		rating.setType(VideoType.getVideoType(videoJson.getString("Type")));
		JSONArray omdbRating = videoJson.getJSONArray("Ratings");
		rating.setRatings(new ArrayList<Rating>());
		for (int i = 0; i < omdbRating.length(); ++i) {
			JSONObject omdbrat = omdbRating.getJSONObject(i);
			Rating dbrating = new Rating();
			dbrating.setSource(SourceType.getSourceType(omdbrat.getString("Source")));
			dbrating.setRating(omdbrat.getString("Value"));
			rating.getRatings().add(dbrating);
		}

	}

}
