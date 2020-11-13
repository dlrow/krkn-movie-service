package com.krkn.movie.msvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.krkn.movie.msvc.config.DbChannel;
import com.krkn.movie.msvc.constants.Constants;
import com.krkn.movie.msvc.db.DbRating;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RatingUpdaterService implements Constants {

	@Autowired
	DbChannel dbChannel;

	@Value("${update.fetchSize}")
	private Integer fetchSize;

	@Autowired
	OmdbService omdbService;

	public void updateRatings() {
		int pageNum = 0;
		List<DbRating> dbRatingsPage = dbChannel.getDbRatingPageSortedBy(pageNum, fetchSize);
		while (dbRatingsPage.size() > 0) {

			dbRatingsPage.forEach(rating -> {
				try {
					if (Integer.valueOf(rating.getYear()) > 2020) {
						String response = omdbService.getOmdbResponse(rating.getTitle(), rating.getYear());
						omdbService.updateRatings(response, rating);
						dbChannel.updateRating(rating);
					}
				} catch (Exception e) {
					log.error(e.toString());
				}

			});
			pageNum++;
			dbRatingsPage = dbChannel.getDbRatingPageSortedBy(pageNum, fetchSize);
		}
	}

//	public void updateRatings() {
//		DbRating video = dbChannel.getVideoByTconst("tt10333912");
//		try {
//			ImdbRating imdbRating = getUpdatedImdbRating(video.getImdbID());
//
//			if (imdbRating != null && imdbRating.getRating() != null) {
//				video.setImdbVotes(imdbRating.getImdbVotes());
//
//				Optional<Rating> dbRating = video.getRatings().stream()
//						.filter(r -> r.getSource().equals(SourceType.IMDB)).findFirst();
//
//				if (dbRating.isPresent()) {
//					dbRating.get().setRating(imdbRating.getRating());
//				} else {
//					video.getRatings().add(new Rating(SourceType.IMDB, imdbRating.getRating()));
//				}
//				dbChannel.ratingSave(video);
//			}
//
//		} catch (IOException e) {
//			log.error(e.toString());
//		}
//
//	}
//
//	private ImdbRating getUpdatedImdbRating(String imdId) throws IOException {
//		if (imdId == null || imdId.length() == 0)
//			return null;
//		String url = "https://www.imdb.com/title/" + imdId;
//
//		Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
//
//		Elements rating = doc.getElementsByAttributeValue("itemprop", "ratingValue");
//		Elements ratingCount = doc.getElementsByAttributeValue("itemprop", "ratingCount");
//
//		ImdbRating imdbbRating = new ImdbRating();
//		imdbbRating.setRating(rating.text() + "/10");
//		imdbbRating.setImdbVotes(ratingCount.text());
//
//		return imdbbRating;
//	}

}
