package com.krkn.movie.msvc.util;

import com.krkn.movie.msvc.config.DbChannel;
import com.krkn.movie.msvc.db.DbVideo;

public class SaveVideoTask extends Thread {

	DbVideo dbVideo;

	DbChannel dbChannel;

	public SaveVideoTask(DbVideo dbVideo, DbChannel dbChannel) {
		super();
		this.dbVideo = dbVideo;
		this.dbChannel = dbChannel;
	}

	@Override
	public void run() {
		dbChannel.save(dbVideo);

	}

}
