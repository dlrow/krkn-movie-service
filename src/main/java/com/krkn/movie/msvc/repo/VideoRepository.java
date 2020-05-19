package com.krkn.movie.msvc.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.krkn.movie.msvc.db.DbVideo;

public interface VideoRepository extends MongoRepository<DbVideo, String> {
	
	DbVideo findByTitle(String title);
	
	DbVideo findByTconst(String tconst);

}