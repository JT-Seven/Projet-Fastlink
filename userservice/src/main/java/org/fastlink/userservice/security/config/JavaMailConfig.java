/*
package org.fastlink.userservice.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
public class JavaMailConfig
{
    @Bean
    public Session sessionConfig()
    {
        String username = "bakuradze@et.intechinfo.fr";
        String password = "Gv3P4TaZu";


        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.auth", "true");


        return Session.getInstance(props, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}
*/
