package fr.pinnackl.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PinnacklMail {

	private static final String SMTP_HOST = "smtp.gmail.com";
	private static final String SMTP_USER = "pinnakle.work@gmail.com";
	private static final String SMTP_PASSWORD = "Icge0ylb!";

	private Properties mailServerProperties;
	private Session getMailSession;

	private void mailServerProperties() {
		// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");
	}

	public void createAccountMail(String email, String pseudo) throws AddressException, MessagingException {

		mailServerProperties();

		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		generateMailMessage.setSubject("Welcome to Pinnackl Calendar");

		StringBuilder builder = new StringBuilder("<html><body>");
		builder.append("<h2>Hello " + pseudo + "</h2><br/>");
		builder.append("<p>Your account on Pinnackl Calendar has been created.</p><br/>");
		builder.append("<p></p><br/>");
		builder.append("</body></html>");

		String emailBody = builder.toString();

		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");

		sendMailProperties(generateMailMessage);

	}

	public void shareEventMail(String email, String pseudo, String event) throws AddressException, MessagingException {

		mailServerProperties();

		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		generateMailMessage.setSubject("New event shared on Pinnackl Calendar");

		StringBuilder builder = new StringBuilder("<html><body>");
		builder.append("<h2>Hello " + pseudo + "</h2><br/>");
		builder.append("<p>The event <b>"+event+"</b> has been shared to you.</p><br/>");
		builder.append("<p></p><br/>");
		builder.append("</body></html>");

		String emailBody = builder.toString();

		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");

		sendMailProperties(generateMailMessage);

	}

	public void changePasswordMail(String email, String pseudo) throws AddressException, MessagingException {

		mailServerProperties();

		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		generateMailMessage.setSubject("Password changed on Pinnackl Calendar");

		StringBuilder builder = new StringBuilder("<html><body>");
		builder.append("<h2>Hello " + pseudo + "</h2><br/>");
		builder.append("<p>Your password account on Pinnackl Calendar has been modified.</p><br/>");
		builder.append("<p></p><br/>");
		builder.append("</body></html>");

		String emailBody = builder.toString();

		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");

		sendMailProperties(generateMailMessage);

	}

	private void sendMailProperties(MimeMessage generateMailMessage) throws MessagingException {

		// Step3
		System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect(SMTP_HOST, SMTP_USER, SMTP_PASSWORD);
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
}
