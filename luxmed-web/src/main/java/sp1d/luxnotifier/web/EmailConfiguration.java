package sp1d.luxnotifier.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {
    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        Properties props = new Properties();
//        props.setProperty("mail.smtp.ssl.enable", "true");
        sender.setJavaMailProperties(props);
//        sender.setHost("smtp.gmail.com");
//        sender.setHost("aspmx.l.google.com");
        sender.setHost("mail.upcpoczta.pl");
        sender.setPort(587);
//        sender.setUsername("buzzband@gmail.com");
//        sender.setPassword("Tango52gaivamebel");
        return sender;
    }
}
