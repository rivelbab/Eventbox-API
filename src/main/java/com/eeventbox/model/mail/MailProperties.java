package com.eeventbox.model.mail;
/**
 * ======================================================
 * Contains all useful properties to send a mail to user
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ======================================================
 */
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

	private SMTP smtp;
	private String from;
	private String fromName;
	private String verificationapi;

	public SMTP getSmtp() {
		return smtp;
	}

	public void setSmtp(SMTP smtp) {
		this.smtp = smtp;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getVerificationapi() {
		return verificationapi;
	}

	public void setVerificationapi(String verificationapi) {
		this.verificationapi = verificationapi;
	}
}
