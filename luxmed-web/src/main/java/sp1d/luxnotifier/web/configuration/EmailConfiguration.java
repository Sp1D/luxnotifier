package sp1d.luxnotifier.web.configuration;

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
        props.setProperty("mail.smtp.ssl.enable", "true");
        sender.setJavaMailProperties(props);
        sender.setHost("smtp.gmail.com");
        sender.setPort(465);
        sender.setUsername("luxmed.notifier@gmail.com");
        sender.setPassword("lubiisanochkivozit");
        return sender;
    }
}
