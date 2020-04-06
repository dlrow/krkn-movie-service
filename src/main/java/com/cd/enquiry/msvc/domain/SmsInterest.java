package com.cd.enquiry.msvc.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "sms")
public class SmsInterest {
	@Id
	private Integer id;

	private String phoneNum;

	private String name;
	
	private String type;

	private String cName;

	private String lName;

	private String sName;
	
	private LocalDateTime date;

	public SmsInterest() {
	}

	public SmsInterest(String phoneNum, String name,String type, String cName, String lName, String sName,LocalDateTime date) {
		super();
		this.phoneNum = phoneNum;
		this.name = name;
		this.type=type;
		this.cName = cName;
		this.lName = lName;
		this.sName = sName;
		this.date=date;
	}
}
