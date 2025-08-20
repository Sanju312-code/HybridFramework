package com.ActionKeywords;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.Utils.PropertyUtil;
import com.reporting.ExtentManager;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailUtil {

//	public static void sendReportByEmail() {
//		try {
//			String reportFile = ExtentManager.getReportPath();
//
//			final String fromEmail = PropertyUtil.getProperty("email.username");
//			final String password = PropertyUtil.getProperty("email.password");
//
//			final String body = PropertyUtil.getProperty("email.body");
//
//			Properties props = new Properties();
//			props.put("mail.smtp.host", "smtp.office365.com");
//			props.put("mail.smtp.port", "587");
//			props.put("mail.smtp.auth", "true");
//			props.put("mail.smtp.starttls.enable", "true");
//
//			// Create session
//			Session session = Session.getInstance(props, new Authenticator() {
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(fromEmail, password);
//				}
//			});
//
//			// Create email message
//			MimeMessage message = new MimeMessage(session);
//			message.setFrom(new InternetAddress(fromEmail));
//
//			// Add TO recipients
//			String[] toList = PropertyUtil.getProperty("email.to").split(",");
//			for (String to : toList) {
//				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.trim()));
//			}
//
//			String ccProperty = PropertyUtil.getProperty("email.cc");
//			if (ccProperty != null && !ccProperty.trim().isEmpty()) {
//				String[] ccList = ccProperty.split(",");
//				for (String cc : ccList) {
//					message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc.trim()));
//				}
//			}
//
//			String subject = PropertyUtil.getProperty("email.subject") + " - "
//					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//			message.setSubject(subject);
//
//			message.setHeader("Message-ID", "<" + UUID.randomUUID().toString() + "@simplify3x.com>");
//			message.removeHeader("In-Reply-To");
//			message.removeHeader("References");
//
//			// Email body + attachment
//			MimeBodyPart messageBodyPart = new MimeBodyPart();
//			messageBodyPart.setText(body);
//
//			MimeBodyPart attachmentPart = new MimeBodyPart();
//			attachmentPart.attachFile(new File(reportFile));
//
//			Multipart multipart = new MimeMultipart();
//			multipart.addBodyPart(messageBodyPart);
//			multipart.addBodyPart(attachmentPart);
//
//			message.setContent(multipart);
//
//			// Send the message
//			Transport.send(message);
//			System.out.println("Email sent successfully with Extent Report!");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Failed to send email.");
//		}
//	}
	public static void sendReportByEmaidyamic() {
		try {
			// Get both report paths from ExtentManager
			String base64ReportFile = ExtentManager.getReportPath64();
			String pngReportFile = ExtentManager.getReportPathpng();

			final String fromEmail = PropertyUtil.getProperty("email.username");
			final String password = PropertyUtil.getProperty("email.password");

			Properties props = new Properties();
			String smtpHost;
			String smtpPort;
			if (fromEmail.toLowerCase().endsWith("@gmail.com")) {
			    smtpHost = "smtp.gmail.com";
			    smtpPort = "587";
			    props.put("mail.smtp.starttls.enable", "true");

			} else if (fromEmail.toLowerCase().endsWith("@outlook.com") ||
			           fromEmail.toLowerCase().endsWith("@hotmail.com") ||
			           fromEmail.toLowerCase().endsWith("@live.com") ||
			           fromEmail.toLowerCase().endsWith("@office365.com") ||
			           fromEmail.toLowerCase().endsWith("@simplify3x.com")) {
			    smtpHost = "smtp.office365.com";
			    smtpPort = "587";
			    props.put("mail.smtp.starttls.enable", "true");

			} else {
			    throw new RuntimeException(
			        "❌ Unsupported email domain: " + fromEmail +
			        ". Please configure SMTP host and port in property file."
			    );
			}


			// Set final SMTP properties
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.auth", "true");
			if (!props.containsKey("mail.smtp.starttls.enable")) {
				String starttls = PropertyUtil.getProperty("email.smtp.starttls.enable");
				if (starttls == null || starttls.trim().isEmpty()) {
					starttls = "true"; // default value
				}
				props.put("mail.smtp.starttls.enable", starttls);
			}

			// Create session
			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			});

			// Create email message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));

			// Add TO recipients
			String[] toList = PropertyUtil.getProperty("email.to").split(",");
			for (String to : toList) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.trim()));
			}

			// Add CC recipients if provided
			String ccProperty = PropertyUtil.getProperty("email.cc");
			if (ccProperty != null && !ccProperty.trim().isEmpty()) {
				String[] ccList = ccProperty.split(",");
				for (String cc : ccList) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc.trim()));
				}
			}

			// Subject with execution date
			String subject = PropertyUtil.getProperty("email.subject") + " - "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			message.setSubject(subject);

			// Build dynamic body with execution details
			StringBuilder emailBody = new StringBuilder();
			emailBody.append("<html><body>");
			emailBody.append("<p>Hello Team,</p>");
			emailBody.append("<p>Please find the automation execution details below:</p>");
			emailBody.append("<p>");
			emailBody.append("<b>Execution Start Time:</b> ").append(ExtentManager.getExecutionStartTime()).append("<br>");
			emailBody.append("<b>Execution End Time:</b> ").append(ExtentManager.getExecutionEndTime()).append("<br>");
			emailBody.append("<b>Execution Duration:</b> ").append(ExtentManager.getExecutionDuration()).append("<br><br>");
			emailBody.append("<b style='color:green;'>Total Passed:</b> <span style='color:green;'>")
			         .append(ExtentManager.getPassedCount()).append("</span><br>");
			emailBody.append("<b style='color:red;'>Total Failed:</b> <span style='color:red;'>")
			         .append(ExtentManager.getFailedCount()).append("</span>");
			emailBody.append("</p>");
			emailBody.append("<p>Reports are attached for your reference.</p>");
			emailBody.append("<p>Thanks,<br>Automation Team</p>");
			emailBody.append("</body></html>");
		

			// Email body
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(emailBody.toString(), "text/html; charset=utf-8");

			// Attach Base64 report
			MimeBodyPart base64Attachment = new MimeBodyPart();
			base64Attachment.attachFile(new File(base64ReportFile));

			// Attach PNG report
			MimeBodyPart pngAttachment = new MimeBodyPart();
			pngAttachment.attachFile(new File(pngReportFile));

			// Combine parts
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(base64Attachment);
			multipart.addBodyPart(pngAttachment);

			message.setContent(multipart);

			// Send the email
			Transport.send(message);
			System.out.println("✅ Email sent successfully with execution details and both Extent Reports!");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("❌ Failed to send email.");
		}
	}
}
