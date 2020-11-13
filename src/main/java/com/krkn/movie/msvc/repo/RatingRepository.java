package com.krkn.movie.msvc.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.krkn.movie.msvc.db.DbRating;

public interface RatingRepository extends MongoRepository<DbRating, String> {

	DbRating findFirstByTitleIgnoreCaseAndYear(String title, String year);

	DbRating findFirstByTitleIgnoreCase(String title);

	DbRating findByImdbID(String tconst);

	DbRating findFirstByUrlIgnoreCase(String url);

}