package com.krkn.movie.msvc.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.krkn.movie.msvc.config.DbChannel;
import com.krkn.movie.msvc.constants.Constants;
import com.krkn.movie.msvc.db.DbVideo;
import com.krkn.movie.msvc.db.Rating;
import com.krkn.movie.msvc.db.SourceType;
import com.krkn.movie.msvc.db.VideoType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VideoService implements Constants {

	@Autowired
	DbChannel dbChannel;

	@Value("${extratext.title.prime}")
	private String primePreTitle;

	@Value("${omdb.url}")
	private String omdbUrl;

	@Value("${omdb.apikey}")
	private String omdbApiKey;

	public DbVideo getVideoByURL(String url) throws IOException {
		String title = extractTitle(url);
		DbVideo dbvideo = getVideoByTitle(title);
		return dbvideo;

	}

	private String extractTitle(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements elem = doc.select("title");
		String title = elem.text().trim();
		if (title.contains(primePreTitle))
			title = title.substring(primePreTitle.length());
		return title;
	}

	public DbVideo getVideoByTitle(String title) throws JsonMappingException, JsonProcessingException {
		if (title == null || !(title.length() > 0))
			throw new RuntimeException("cannot extract title");
		title = title.trim();

		DbVideo dbVideo = dbChannel.getVideoByTitle(title);

		if (dbVideo == null) {
			String response = getOmdbResponse(title);
			dbVideo = omdbResponseToDbVideoMapper(response);
			dbChannel.save(dbVideo);
		}
		return dbVideo;
	}

	private String getOmdbResponse(String title) {
		log.info("calling omdb api with title: " + title);
		String url = omdbUrl + omdbApiKey + "&t=" + title;
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url, String.class);
		return result;
	}

	private DbVideo omdbResponseToDbVideoMapper(String response)
			throws JsonMappingException, JsonProcessingException {
		DbVideo dbVideo = new DbVideo();
		JSONObject videoJson = new JSONObject(response);
		dbVideo.setTitle(String.valueOf(videoJson.get("Title")));
		dbVideo.setAwards(String.valueOf(videoJson.get("Awards")));
		dbVideo.setGenre(String.valueOf(videoJson.get("Genre")));
		dbVideo.setLanguage(String.valueOf(videoJson.get("Language")));
		dbVideo.setPlot(String.valueOf(videoJson.get("Plot")));
		dbVideo.setPoster(String.valueOf(videoJson.get("Poster")));
		dbVideo.setRated(String.valueOf(videoJson.get("Rated")));
		dbVideo.setReleasedDate(String.valueOf(videoJson.get("Released")));
		dbVideo.setRuntime(String.valueOf(videoJson.get("Runtime")));
		dbVideo.setYear(String.valueOf(videoJson.get("Year")));

		return dbVideo;
	}

	public void createVideoFromTsv(String basicPath) throws IOException {

		Map<String, DbVideo> videos = new HashMap<>();

		BufferedReader basicbuffer = new BufferedReader(new FileReader(basicPath));
		String basicRead;
		basicbuffer.readLine();
		while ((basicRead = basicbuffer.readLine()) != null) {
			String splitarray[] = basicRead.split("\t");
			String tconst = splitarray[0];
			String type = splitarray[1];
			String title = splitarray[3];
			String runMin = splitarray[7];
			String genres = splitarray[8];
			if (!videos.containsKey(tconst)) {
				DbVideo v = new DbVideo();
				v.setGenre(genres);
				v.setTconst(tconst);
				v.setTitle(title);
				v.setType(getType(type));

				v.setRuntime(runMin);
				videos.put(tconst, v);
			}

			if (videos.size() >= CHUNKN_SIZE) {
				addMapToDB(videos);
				videos = new HashMap<String, DbVideo>();
			}

		}

		addMapToDB(videos);
		videos = null;

		basicbuffer.close();

	}

	private void addMapToDB(Map<String, DbVideo> videos) {

		videos.forEach((k, v) -> {
			dbChannel.save(v);

		});
	}

	private VideoType getType(String type) {
		String t = type.toLowerCase();
		if (t.contains("movie"))
			return VideoType.MOVIE;
		if (t.contains("episode"))
			return VideoType.EPISODE;
		if (t.contains("series"))
			return VideoType.SERIES;
		if (t.contains("short"))
			return VideoType.SHORT;
		if (t.contains("docum"))
			return VideoType.DOCUMENTARY;
		else
			return VideoType.DEFAULT;

	}

	public void addImdbRatings(String ratingPath) throws IOException {

		Map<String, Rating> ratingMap = new HashMap<>();

		BufferedReader ratingbuffer = new BufferedReader(new FileReader(ratingPath));
		String ratingRead;
		ratingbuffer.readLine();
		while ((ratingRead = ratingbuffer.readLine()) != null) {
			String splitarray[] = ratingRead.split("\t");
			String tconst = splitarray[0];
			String rating = splitarray[1];
			String votes = splitarray[2];
			if (!ratingMap.containsKey(tconst)) {
				ratingMap.put(tconst, new Rating(SourceType.IMDB, rating, votes));
			}

			if (ratingMap.size() >= CHUNKN_SIZE) {
				addRatingToDB(ratingMap);
				ratingMap = new HashMap<String, Rating>();
			}

		}

		addRatingToDB(ratingMap);
		ratingMap = null;
		ratingbuffer.close();

	}

	private void addRatingToDB(Map<String, Rating> videos) {
		videos.forEach((k, v) -> {
			DbVideo video = dbChannel.getVideoByTconst(k);
			if (video != null) {
				video.getRatings().add(v);
				dbChannel.save(video);
			}
		});

	}

}
