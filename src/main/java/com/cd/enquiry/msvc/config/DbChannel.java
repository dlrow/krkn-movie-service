package com.cd.enquiry.msvc.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cd.enquiry.msvc.domain.SmsInterest;
import com.cd.enquiry.msvc.domain.Student;
import com.cd.enquiry.msvc.repo.SmsInterestRepository;
import com.cd.enquiry.msvc.repo.StudentRepository;

@Repository
public class DbChannel {
	@Autowired
	StudentRepository studentRepo;

	@Autowired
	SmsInterestRepository smsInterestRepo;

	public void saveEnquiry(Student st) {
		studentRepo.save(st);
	}

	public List<Student> getStudentListWhoEnquiredAfter(LocalDateTime date) {
		return studentRepo.findByEnquiryDateAfter(date);
	}

	public void saveInterest(SmsInterest sm) {
		smsInterestRepo.save(sm);
	}

	public List<SmsInterest> getSmsIntrestAfter(LocalDateTime date) {
		return smsInterestRepo.findByDateAfter(date);
	}
	

}
