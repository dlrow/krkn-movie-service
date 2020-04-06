package com.cd.enquiry.msvc.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cd.enquiry.msvc.config.DbChannel;
import com.cd.enquiry.msvc.domain.Student;
import com.cd.enquiry.msvc.requestdto.MailObj;
import com.cd.enquiry.msvc.util.CDMailService;
import com.cd.enquiry.msvc.util.ExcelUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EnquiryService {

	@Autowired
	DbChannel dbChannel;

	@Autowired
	CDMailService cdMailService;

	@Autowired
	ExcelUtil excelUtil;

	@Value("${enquiry.mail.to}")
	private String to;

	public void saveEnquiry(String name, String email, String contact, String course) {
		log.debug("saveEnquiry of EnquiryService called ");
		Student st = new Student(name, email, contact, course, LocalDateTime.now());
		dbChannel.saveEnquiry(st);

	}

	public void sendMailForInterestedCandidates(LocalDateTime date) throws IOException {
		List<Student> studentList = dbChannel.getStudentListWhoEnquiredAfter(date);
		MailObj mobj = new MailObj();
		mobj.setContent("Please find attached list of students who enquired for CosDelta Academy after " + date);
		mobj.setTo(to);
		mobj.setSubject("List of Interested Students");
		mobj.setDataSource(excelUtil.createExcelToMailListOfEnquiry(studentList));
		try {
			cdMailService.sendEmail(mobj, extractToList(to));
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	private String[] extractToList(String to) {
		String arr[] = to.split(",");
		return arr;
	}

}
