package org.ezapi.util;

import com.sun.mail.util.MailSSLSocketFactory;
import org.ezapi.html.PageComponent;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public final class MailUtils {

    public static void send(String senderMail, String smtpHost, int smtpPort, String senderPassword, String senderName, String receiverMail, String subject, String content) {
        send(senderMail, smtpHost, smtpPort, senderPassword, senderName, subject, content, new String[] { receiverMail });
    }

    public static void send(String senderMail, String smtpHost, int smtpPort, String senderPassword, String senderName, String receiverMail, String subject, PageComponent pageComponent) {
        send(senderMail, smtpHost, smtpPort, senderPassword, senderName, subject, pageComponent, receiverMail);
    }

    public static void send(String senderMail, String smtpHost, int smtpPort, String senderPassword, String senderName, String subject, PageComponent pageComponent, String... receivers) {
        send(senderMail, smtpHost, smtpPort, senderPassword, senderName, subject, pageComponent.parseToString(), receivers);
    }

    public static void send(String senderMail, String smtpHost, int smtpPort, String senderPassword, String senderName, String subject, String content, String... receivers) {
        if (receivers.length == 0) return;
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", smtpHost);
        properties.setProperty("mail.smtp.port", String.valueOf(smtpPort));
        properties.setProperty("mail.smtp.auth", "true");
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Session session = Session.getInstance(properties);
        session.setDebug(false);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(senderMail, senderName, "UTF-8"));
            List<InternetAddress> list = new ArrayList<>();
            for (String receiverMail : receivers) {
                list.add(new InternetAddress(receiverMail, receiverMail, "UTF-8"));
            }
            message.setRecipients(MimeMessage.RecipientType.TO, list.toArray(new InternetAddress[0]));
            message.setSubject(subject, "UTF-8");
            message.setContent(content, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport = session.getTransport();
            transport.connect(senderMail, senderPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException | UnsupportedEncodingException ignored) {
        }
    }

}
