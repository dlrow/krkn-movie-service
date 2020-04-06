package com.cd.enquiry.msvc.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cd.enquiry.msvc.requestdto.MailObj;

@Service
public class CDMailService {

	@Autowired
	private JavaMailSender emailSender;

	public void sendEmail(MailObj mail, String[] to) throws MessagingException {
		// TODO: make it multi-threaded
		for (int i = 0; i < to.length; i++) {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setSubject(mail.getSubject());
			helper.setText(mail.getContent());
			helper.setTo(to[i]);
			if (mail.getDataSource() != null)
				helper.addAttachment("Students_Enquired_After.xls", mail.getDataSource());

			emailSender.send(message);
		}

	}

}
