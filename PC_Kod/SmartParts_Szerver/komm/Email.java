import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Email
{
	public static void SMTPKuld(String Cim, String Targy, String Uzenet)
	{
		try
		{
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props, new javax.mail.Authenticator()
			{
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(FoClass.Emailusername, FoClass.Emailpassword);
				}
			});

			try
			{

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(FoClass.Emailusername));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Cim));
				message.setSubject(Targy);
				message.setText(Uzenet);

				Transport.send(message);

				System.out.println("SMTP Mail sent to: " + Cim);

			}
			catch (MessagingException e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception ex)
		{
			ex.getMessage();
		}
	}
}
