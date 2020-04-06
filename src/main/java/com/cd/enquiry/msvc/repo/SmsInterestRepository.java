package com.cd.enquiry.msvc.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cd.enquiry.msvc.domain.SmsInterest;


public interface SmsInterestRepository extends MongoRepository<SmsInterest, String> {
	
	List<SmsInterest> findByDateAfter(LocalDateTime date);
	


}
