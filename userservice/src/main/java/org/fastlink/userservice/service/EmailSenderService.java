package org.fastlink.userservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.model.RegistrationVerificationToken;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service("emailSenderService")
@AllArgsConstructor
@Slf4j
public class EmailSenderService
{

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(SimpleMailMessage mail)
    {
        this.javaMailSender.send(mail);
    }

    public SimpleMailMessage createConfirmationMail(FastlinkUser user, RegistrationVerificationToken token)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration to Fastlink!");
        mailMessage.setFrom("bakuradze@et.intechinfo.fr");
        mailMessage.setText("To activate your account, please click here : "
        + "http://localhost:8083/api/v1/users/activate-account?token=" + token.getToken());

        return mailMessage;
    }

    public SimpleMailMessage createPasswordNotificationMail(FastlinkUser user, String password)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Your Fastlink password");
        mailMessage.setFrom("org.fastlink@gmail.com");
        mailMessage.setText("Here is your Fastlink password: " + password + "\n\nEnjoy :)");

        //MimeMailMessage mimeMailMessage = new MimeMailMessage(new MimeMessageHelper(new MimeMessa));

        return mailMessage;
    }
}
