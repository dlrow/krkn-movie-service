package com.krkn.movie.msvc.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krkn.movie.msvc.config.DbChannel;
import com.krkn.movie.msvc.constants.Constants;
import com.krkn.movie.msvc.db.Rating;
import com.krkn.movie.msvc.db.SourceType;
import com.krkn.movie.msvc.db.Video;
import com.krkn.movie.msvc.db.VideoType;

@Service
public class VideoService implements Constants {

	@Autowired
	DbChannel dbChannel;

	public Video getVideoByTitle(String title) {
		Video v = dbChannel.getVideoByTitle(title);
		if (v == null)
			throw new RuntimeException("Video :" + title + " not found");
		return v;

	}

	public void createVideoFromTsv(String basicPath) throws IOException {

		Map<String, Video> videos = new HashMap<>();

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
				Video v = new Video();
				v.setGenre(genres);
				v.setTconst(tconst);
				v.setTitle(title);
				v.setType(getType(type));
				v.setTime(Integer.valueOf(runMin));
				videos.put(tconst, v);
			}

			if (videos.size() >= CHUNKN_SIZE) {
				addMapToDB(videos);
				videos = new HashMap<String, Video>();
			}

		}

		addMapToDB(videos);
		videos = null;

		basicbuffer.close();

	}

	private void addMapToDB(Map<String, Video> videos) {
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
			Video video = dbChannel.getVideoByTconst(k);
			if (video != null) {
				video.getRatings().add(v);
				dbChannel.save(video);
			}
		});

	}

}
