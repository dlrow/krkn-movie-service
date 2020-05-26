package com.krkn.movie.msvc.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractTitleYearUtil {

	public static VideoTitleYear getTitleYear(String url) throws IOException {

		VideoTitleYear vty = null;
		Document doc = Jsoup.connect(url).get();
		if (url.contains("primevideo"))
			vty = primeVideoTitleYear(doc);
		else if (url.contains("hotstar"))
			vty = hotstarVideoTitleYear(doc);
		else if (url.contains("netflix"))
			vty = hotstarVideoTitleYear(doc);

		else
			throw new RuntimeException("invalid url: " + url);

		return vty;
	}

	private static VideoTitleYear primeVideoTitleYear(Document doc) {
		VideoTitleYear vty = new VideoTitleYear();
		Elements h1Elements = doc.select("h1");
		Elements spanElements = doc.select("span");
		for (Element h1 : h1Elements) {
			if (String.valueOf(h1.attributes()).contains("title"))
				vty.setTitle(h1.text());

		}

		for (Element span : spanElements) {
			if (String.valueOf(span.attributes()).contains("release-year-badge"))
				vty.setYear(span.text());

		}
		return vty;
	}

	private static VideoTitleYear hotstarVideoTitleYear(Document doc) {
		VideoTitleYear vty = new VideoTitleYear();
		Elements h1Elements = doc.select("h1");
		Elements spanElements = doc.select("span");
		for (Element h1 : h1Elements) {
			if (String.valueOf(h1.attributes()).contains("title"))
				vty.setTitle(h1.text());

		}

		for (Element span : spanElements) {
			if (String.valueOf(span.attributes()).contains("release-year-badge"))
				vty.setYear(span.text());

		}
		return vty;
	}

}
