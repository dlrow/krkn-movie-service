package com.krkn.movie.msvc.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.krkn.movie.msvc.db.DbRating;
import com.krkn.movie.msvc.repo.RatingRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DbChannel {

	@Autowired
	RatingRepository ratingRepo;

	public List<DbRating> getDbRatingPageSortedBy(int pageNo, int pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<DbRating> pagedResult = ratingRepo.findAll(paging);
		return pagedResult.getContent();
	}

	public List<DbRating> getAllRatings() {
		return ratingRepo.findAll();
	}

	public DbRating getRatingByTitleYr(String title, String year) {
		log.info("DBChannel :getRatingByTitleYr: " + title + year);
		if (year == "")
			return ratingRepo.findFirstByTitleIgnoreCase(title);
		return ratingRepo.findFirstByTitleIgnoreCaseAndYear(title, year);
	}

	public DbRating getRatingByUrl(String url) {
		log.info("DBChannel :getVideoByUrl: " + url);
		return ratingRepo.findFirstByUrlIgnoreCase(url);
	}

	public DbRating getVideoByTconst(String tconst) {
		return ratingRepo.findByImdbID(tconst);
	}

	public synchronized DbRating saveWithDuplicateCheck(DbRating rating) {
		String title = rating.getTitle();
		String year = rating.getYear();
		if (this.getRatingByTitleYr(title, year) == null)
			return ratingRepo.save(rating);
		else
			return rating;

	}

	public DbRating updateRating(DbRating rating) {
		return ratingRepo.save(rating);
	}

}
