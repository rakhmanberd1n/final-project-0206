package nazym.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService
{
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String subject, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nazym_0901@mail.ru");
        message.setSubject(subject);
        message.setText(body);
        message.setTo(toEmail);
        javaMailSender.send(message);
    }

}