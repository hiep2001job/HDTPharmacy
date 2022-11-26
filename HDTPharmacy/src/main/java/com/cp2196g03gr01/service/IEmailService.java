package com.cp2196g03gr01.service;

import com.cp2196g03gr01.entity.EmailDetail;

public interface IEmailService {
	// Method
	// To send a simple email
	String sendSimpleMail(EmailDetail details);

	// Method
	// To send an email with attachment
	String sendMailWithAttachment(EmailDetail details);
}
