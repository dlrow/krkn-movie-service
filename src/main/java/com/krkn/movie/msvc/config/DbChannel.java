package com.krkn.movie.msvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.krkn.movie.msvc.db.DbVideo;
import com.krkn.movie.msvc.repo.VideoRepository;

@Repository
public class DbChannel {
	
	@Autowired
	VideoRepository videoRepo;

	public DbVideo getVideoByTitle(String title) {
		return videoRepo.findFirstByTitleIgnoreCase(title);
	}

	public DbVideo getVideoByTconst(String tconst) {
		return videoRepo.findByTconst(tconst);
	}

	public synchronized DbVideo save(DbVideo video) {
		String title = video.getTitle();
		if (this.getVideoByTitle(title) == null)
			return videoRepo.save(video);
		else
			return video;

	}

}
