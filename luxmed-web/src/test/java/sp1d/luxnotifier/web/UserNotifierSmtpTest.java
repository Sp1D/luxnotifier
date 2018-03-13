package sp1d.luxnotifier.web;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sp1d.luxnotifier.web.configuration.EmailConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmailConfiguration.class)
public class UserNotifierSmtpTest {
    @Autowired
    private MailSender mailSender;

    @Test
    @Ignore
    public void sendsMessageViaRealSmtpHost() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("buzzband@gmail.com");
        msg.setFrom("luxmed.notifier@gmail.com");
        msg.setSubject("Test email from luxnotifier");
        msg.setText("Subj. lek. med. Gregorz Brzęczyszczykiewicz");
        mailSender.send(msg);
    }
}