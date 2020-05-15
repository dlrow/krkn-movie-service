package com.krkn.movie.msvc.db;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "video")
public class Video {
	
	@Id
	private String tconst;

	@Indexed
	String title;
	
	String genre;
	LocalDateTime releaseDate;
	String trailerLink;
	Integer time;
	VideoType type;
	
	List<Rating> ratings;
	List<Video> videoIds;
	List<Crew> crews;
	List<Review> reviewIds;

	// List<Languages> languages;
	public Video() {
		ratings = new ArrayList<Rating>();
	}

}
