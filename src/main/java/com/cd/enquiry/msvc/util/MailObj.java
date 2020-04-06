package com.cd.enquiry.msvc.util;

import javax.activation.DataSource;

import lombok.Data;

@Data
public class MailObj {

	private String from;
	private String to;
	private String subject;
	private String content;
	private DataSource dataSource;

}
