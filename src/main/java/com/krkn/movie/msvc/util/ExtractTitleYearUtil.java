package com.krkn.movie.msvc.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.krkn.movie.msvc.pojo.VideoTitleYear;

public class ExtractTitleYearUtil {

	public static VideoTitleYear getTitleYear(String url) throws IOException {

		VideoTitleYear vty = null;

		if (url.contains("primevideo"))
			vty = primeVideoTitleYear(url);
		else if (url.contains("hotstar"))
			vty = hotstarVideoTitleYear(url);
		else if (url.contains("netflix"))
			vty = netflixVideoTitleYear(url);

		else
			throw new RuntimeException("invalid url: " + url);

		return vty;
	}

	private static VideoTitleYear primeVideoTitleYear(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		VideoTitleYear vty = new VideoTitleYear();
		Elements h1Elements = doc.select("h1");
		Elements spanElements = doc.select("span");
		for (Element h1 : h1Elements) {
			if (String.valueOf(h1.attributes()).contains("title")) {
				vty.setTitle(h1.text());
				break;
			}

		}

		for (Element span : spanElements) {
			if (String.valueOf(span.attributes()).contains("release-year-badge"))
				vty.setYear(span.text());

		}
		return vty;
	}

	private static VideoTitleYear hotstarVideoTitleYear(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		VideoTitleYear vty = new VideoTitleYear();
		Elements titleElements = doc.getElementsByAttributeValueContaining("class", "toptitle clear-both");
		Elements yearElement = doc.getElementsByAttributeValueContaining("class", "meta-data");
		vty.setTitle(titleElements.text());
		if (titleElements.size() == 0)
			vty.setTitle(doc.select("h1").text());

		for (Element span : yearElement) {
			Elements spanChild = span.getAllElements();
			for (Element e : spanChild) {
				if (String.valueOf(e.text()).matches("^(19|20)\\d{2}$")) {
					vty.setYear(e.text());
					return vty;
				}
			}

		}

		return vty;
	}

	private static VideoTitleYear netflixVideoTitleYear(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		VideoTitleYear vty = new VideoTitleYear();
		Elements titleElements = doc.getElementsByAttributeValueContaining("class", "title-title");
		Elements yearElement = doc.getElementsByAttributeValueContaining("class", "item-year");
		vty.setTitle(titleElements.text());

		for (Element span : yearElement) {
			if (String.valueOf(span.text()).matches("^(19|20)\\d{2}$"))
				vty.setYear(span.text());

		}
		return vty;
	}

}
