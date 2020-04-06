package com.cd.enquiry.msvc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cd.enquiry.msvc.config.DbChannel;
import com.cd.enquiry.msvc.constants.CDConstants;
import com.cd.enquiry.msvc.util.ExcelForSmsUtil;
import com.cd.enquiry.msvc.util.OtpHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SmsService implements CDConstants {

	@Autowired
	DbChannel dbChannel;

	@Autowired
	ExcelForSmsUtil s3Service;

	@Autowired
	OtpHelper otpHelper;

	@Value("${sms.msg91.authkey}")
	String authkey;

	public String sendPromotional(MultipartFile file, String message) {
		String mobiles = readMobileNumbersFromExcel(file);
		if (mobiles.length() < 10)
			return "no mobile number found";
		mobiles = mobiles.substring(1, mobiles.length() - 1);
		sendUsingMg91(mobiles, message);
		return "messages sent";
	}

	public void sendUsingMg91(String mobiles, String message) {

		// Sender ID,While using route4 sender id should be 6 characters long.
		String senderId = SENDERID;

		String route = ROUTE;

		URLConnection myURLConnection = null;
		URL myURL = null;
		BufferedReader reader = null;

		String encoded_message = URLEncoder.encode(message);

		String mainUrl = MAINURL;

		StringBuilder sbPostData = new StringBuilder(mainUrl);
		sbPostData.append("authkey=" + authkey);
		sbPostData.append("&mobiles=" + mobiles);
		sbPostData.append("&message=" + encoded_message);
		sbPostData.append("&route=" + route);
		sbPostData.append("&sender=" + senderId);
		sbPostData.append("&country=" + "91");
		mainUrl = sbPostData.toString();
		try {
			// prepare connection
			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			String response;
			while ((response = reader.readLine()) != null)
				log.info(response);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String readMobileNumbersFromExcel(MultipartFile file) {
		String mobiles = "";
		try {
			mobiles = s3Service.readFileMultiPart(file);
		} catch (IOException e) {
			log.error("Error in reading mobile numbers rom excel", e);
		}
		return mobiles;
	}

	public String sendOtp(String phoneNumber, String ip) {
		Integer otp;
		if(otpHelper.isScam(phoneNumber,ip))
			return "Max otp reached for phone number "+phoneNumber;
		Map<String, Integer> m = otpHelper.getOtpMap();
		
		if (m.get(phoneNumber) == null) {
			otp = getRandamOtp();
			m.put(phoneNumber, otp);
		} else
			otp = m.get(phoneNumber);

		String message = OTP_MESSAGE + otp;
		sendUsingMg91(phoneNumber, message);
		return "otp sent";
	}

	public Boolean verifyOtp(String phoneNumber, Integer otp) {
		Map<String, Integer> m = otpHelper.getOtpMap();
		if (m.get(phoneNumber) == null) {
			return false;
		}
		if (m.get(phoneNumber).equals(otp)) {
			m.remove(phoneNumber);
			return true;
		} else
			return false;
	}

	private Integer getRandamOtp() {
		int random = (int) (Math.random() * 8999 + 1);
		random += 1001;
		return random;
	}

	public String cleanCache() {
		otpHelper.cleanCache();
		return "cleaned";
	}

	public void sendMailForInterestedCandidates(LocalDateTime date) {
		try {
			s3Service.sendMailForInterestedCandidates(date);
		} catch (IOException e) {
			log.error("Error while sending mail for interested people", e);
		}
	}

	public String getCache() {
		return otpHelper.getCache();
	}
}
