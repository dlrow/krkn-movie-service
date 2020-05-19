package com.krkn.movie.msvc.db;

public enum VideoType {
	MOVIE, SERIES, EPISODE, SHORT, DOCUMENTARY, DEFAULT;

	public static VideoType getVideoType(String source) {
		source = source.toLowerCase();
		if (source.contains("movie") || source.contains("imdb"))
			return VideoType.MOVIE;
		if (source.contains("series"))
			return VideoType.SERIES;
		if (source.contains("episode"))
			return VideoType.EPISODE;
		if (source.contains("short"))
			return VideoType.SHORT;
		if (source.contains("doc"))
			return VideoType.DOCUMENTARY;
		else
			return VideoType.DEFAULT;
	}
}
