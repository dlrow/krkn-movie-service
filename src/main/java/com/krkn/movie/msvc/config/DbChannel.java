package com.krkn.movie.msvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.krkn.movie.msvc.db.DbVideo;
import com.krkn.movie.msvc.repo.VideoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DbChannel {

	@Autowired
	VideoRepository videoRepo;

	public DbVideo getVideoByTitle(String title) {
		log.info("DBChannel :getVideoByTitle: " + title);
		return videoRepo.findFirstByTitleIgnoreCase(title);
	}

	public DbVideo getVideoByUrl(String url) {
		log.info("DBChannel :getVideoByUrl: " + url);
		return videoRepo.findFirstByUrlIgnoreCase(url);
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
