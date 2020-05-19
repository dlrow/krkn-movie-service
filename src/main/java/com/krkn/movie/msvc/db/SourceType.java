package com.krkn.movie.msvc.db;

public enum SourceType {
	IMDB, ROTTEN_TOMATO, METACRITIC, DEFAULT;

	public static SourceType getSourceType(String source) {
		source = source.toLowerCase();
		if (source.contains("internet") || source.contains("imdb"))
			return SourceType.IMDB;
		if (source.contains("rotten"))
			return SourceType.ROTTEN_TOMATO;
		if (source.contains("metacr"))
			return SourceType.METACRITIC;
		else
			return SourceType.DEFAULT;
	}
}
