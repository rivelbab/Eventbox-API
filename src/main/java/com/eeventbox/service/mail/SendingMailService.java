package com.eeventbox.service.mail;
/**
 * ================================================
 * Contains all useful operations for the User
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * Updated by Rivelbab on 28/10/2019 at Nanterre U.
 * ================================================
 */
public interface SendingMailService {

	boolean sendVerificationMail(String toEmail, String verifUrl, String subject,  String emailMsg);
}
