package com.krkn.movie.msvc.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.krkn.movie.msvc.db.Video;

public interface VideoRepository extends MongoRepository<Video, String> {
	
	Video findByTitle(String title);
	
	Video findByTconst(String tconst);

}