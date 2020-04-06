package com.cd.enquiry.msvc.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cd.enquiry.msvc.domain.Student;

public interface StudentRepository extends MongoRepository<Student, String> {
	
	List<Student> findByEnquiryDateAfter(LocalDateTime date);

}