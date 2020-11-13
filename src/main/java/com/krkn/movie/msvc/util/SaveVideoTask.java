package com.krkn.movie.msvc.util;

import com.krkn.movie.msvc.config.DbChannel;
import com.krkn.movie.msvc.db.DbRating;

public class SaveVideoTask extends Thread {

	DbRating dbVideo;

	DbChannel dbChannel;

	public SaveVideoTask(DbRating dbRating, DbChannel dbChannel) {
		super();
		this.dbVideo = dbRating;
		this.dbChannel = dbChannel;
	}

	@Override
	public void run() {
		dbChannel.saveWithDuplicateCheck(dbVideo);

	}

}
