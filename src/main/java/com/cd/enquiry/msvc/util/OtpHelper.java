package com.cd.enquiry.msvc.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OtpHelper {

	int LIMIT = 20;
	Queue<String> phoneNumberQueue = new LinkedList<>();
	Queue<String> ipQueue = new LinkedList<>();
	Map<String, Integer> otpCountMap = new HashMap<>(20);
	Map<String, Integer> ipCountMap = new HashMap<>(20);
	Map<String, Integer> otpMap;

	public Map<String, Integer> getOtpMap() {
		if (otpMap == null) {
			otpMap = new HashMap<>();
		}
		return otpMap;
	}

	public boolean isScam(String phone, String ip) {
		updateCounter(phone);
		updateCounterIp(ip);
		return (otpCountMap.containsKey(phone) && (otpCountMap.get(phone) > 3)
				|| ipCountMap.containsKey(phone) && (ipCountMap.get(phone) > 4));
	}

	private void updateCounterIp(String ip) {
		log.info("updateCounterIp of OtpHelper called for phone ", ip);
		while (ipQueue.size() >= LIMIT) {
			String s = ipQueue.remove();
			ipCountMap.remove(s);
		}
		ipQueue.add(ip);
		if (ipCountMap.containsKey(ip)) {
			ipCountMap.put(ip, ipCountMap.get(ip) + 1);
		} else {
			ipCountMap.put(ip, 1);
		}
	}

	private void updateCounter(String phone) {
		log.info("updateCounter of OtpHelper called for phone ", phone);
		while (phoneNumberQueue.size() >= LIMIT) {
			String s = phoneNumberQueue.remove();
			otpCountMap.remove(s);
		}
		phoneNumberQueue.add(phone);
		if (otpCountMap.containsKey(phone)) {
			otpCountMap.put(phone, otpCountMap.get(phone) + 1);
		} else {
			otpCountMap.put(phone, 1);
		}
	}

	public void cleanCache() {
		log.info("clean of OtpHelper called");
		otpMap = new HashMap<>();
		phoneNumberQueue = new LinkedList<>();
		otpCountMap = new HashMap<>();
		ipCountMap = new HashMap<>();
		ipQueue = new LinkedList<>();
	}

	public String getCache() {
		return otpMap.toString() + "\n\n" + otpCountMap.toString() + "\n\n" + phoneNumberQueue.toString() + "\n\n"
				+ ipCountMap.toString() + "\n\n" + ipQueue.toString();
	}

}
