package com.krkn.movie.msvc.db;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Rating {

	SourceType source;
	String rating;
	String numOfVotes;

	public Rating() {

	}

	public Rating(SourceType source, String rating, String numOfVotes) {
		super();
		this.source = source;
		this.rating = rating;
		this.numOfVotes = numOfVotes;
	}

}
