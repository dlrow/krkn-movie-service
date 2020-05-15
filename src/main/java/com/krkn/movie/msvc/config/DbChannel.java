package com.krkn.movie.msvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.krkn.movie.msvc.db.Video;
import com.krkn.movie.msvc.repo.VideoRepository;

@Repository
public class DbChannel {
	@Autowired
	VideoRepository videoRepo;

	public Video getVideoByTitle(String title) {
		return videoRepo.findByTitle(title);
	}

	public Video getVideoByTconst(String tconst) {
		return videoRepo.findByTconst(tconst);
	}
	
	public Video save(Video video) {
		return videoRepo.save(video);
	}

}
