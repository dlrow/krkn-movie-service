package com.cd.enquiry.msvc.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "student")
public class Student {
	@Id
	private String id;

	private String name;

	private String email;

	private String contact;

	private String course;

	private LocalDateTime enquiryDate;

	public Student() {
	}

	public Student(String name, String email, String contact, String course, LocalDateTime enquiryDate) {
		this.name = name;
		this.email = email;
		this.contact = contact;
		this.enquiryDate = enquiryDate;
		this.course = course;

	}

}
