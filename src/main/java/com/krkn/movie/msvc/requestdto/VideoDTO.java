package com.krkn.movie.msvc.requestdto;

import java.time.LocalDateTime;
import java.util.List;

import com.krkn.movie.msvc.db.Crew;
import com.krkn.movie.msvc.db.DbVideo;
import com.krkn.movie.msvc.db.Rating;
import com.krkn.movie.msvc.db.Review;
import com.krkn.movie.msvc.db.VideoType;

import lombok.Data;

@Data
public class VideoDTO {

	String title;
	String year;
	String rated;
	String releasedDate;
	String runtime;
	String plot;
	String language;
	String awards;
	String genre;
	LocalDateTime releaseDate;
	String trailerLink;
	Integer time;
	VideoType type;
	List<Rating> ratings;
	List<DbVideo> videoIds;
	List<Crew> crews;
	List<Review> reviewIds;

}
