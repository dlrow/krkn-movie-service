package com.krkn.movie.msvc.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document(collection = "video")
public class DbVideo {

	@Id
	@JsonIgnore
	private String tconst;

	@Indexed
	String title;
	String year;
	String rated;
	String releasedDate;
	String runtime;
	String plot;
	String language;
	String awards;
	String country;
	String genre;
	String trailerLink;
	String poster;
	String imdbVotes;
	VideoType type;
	List<Rating> ratings;
	List<DbVideo> videoIds;
	List<Crew> crews;
	List<Review> reviewIds;

	// List<Languages> languages;
	public DbVideo() {
		ratings = new ArrayList<Rating>();
	}

}
