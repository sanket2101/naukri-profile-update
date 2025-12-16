package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.List;
import java.util.Properties;

public class EmailUtil {

    public static void sendEmailWithAttachments(
            List<File> screenshots,
            File extentReport
    ) {

        String from = System.getenv("EMAIL_USER");
        String pass = System.getenv("EMAIL_PASS");
        String to = from;

        if (from == null || pass == null) {
            System.out.println("Email credentials missing");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        try {
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, pass);
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject("Automation Execution Report");

            Multipart multipart = new MimeMultipart();

            // Email body
            MimeBodyPart body = new MimeBodyPart();
            body.setText(
                    "Hi,\n\nAutomation execution completed.\n\n" +
                            "Please find attached:\n" +
                            "1. Extent Report\n" +
                            "2. Screenshots\n\nRegards"
            );
            multipart.addBodyPart(body);

            // Attach Extent report
            if (extentReport.exists()) {
                MimeBodyPart reportPart = new MimeBodyPart();
                reportPart.attachFile(extentReport);
                multipart.addBodyPart(reportPart);
            } else {
                System.out.println("Extent report not found: " + extentReport.getAbsolutePath());
            }

            // Attach screenshots
            for (File file : screenshots) {
                if (file.exists()) {
                    MimeBodyPart attach = new MimeBodyPart();
                    attach.attachFile(file);
                    multipart.addBodyPart(attach);
                }
            }

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("Email sent with Extent report + screenshots");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
