package sp1d.luxnotifier.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import sp1d.luxnotifier.entity.Subscription;
import sp1d.luxnotifier.parser.AvailableVisit;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class UserNotifier {
    private static final Logger LOG = LoggerFactory.getLogger(UserNotifier.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private final MailSender mailSender;

    @Autowired
    public UserNotifier(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notifyUser(Subscription subscription, List<AvailableVisit> availableVisits) {
        if (availableVisits == null || availableVisits.isEmpty()) {
            return;
        }
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(subscription.getUserEmail());
        msg.setFrom("luxmed.notifier@gmail.com");
        formatSubject(msg, availableVisits);
        formatBody(msg, availableVisits);

        mailSender.send(msg);
        LOG.info("Notifying email is sent to {}", msg.getTo()[0]);
    }

    private void formatSubject(SimpleMailMessage msg, List<AvailableVisit> availableVisits) {
        String subject = String.format("Luxnotifier: new visit available, %s, %s",
                availableVisits.get(0).getDateTime().format(DATE_TIME_FORMATTER),
                availableVisits.get(0).getService());
        msg.setSubject(subject);
    }

    private void formatBody(SimpleMailMessage msg, List<AvailableVisit> availableVisits) {
        String body = String.format("New visits are available:\n%s\nPlease proceed to Luxmed site in order to book a visit.",
                formatVisitsInformation(availableVisits));
        msg.setText(body);
    }

    private StringBuilder formatVisitsInformation(List<AvailableVisit> visits) {
        StringBuilder builder = new StringBuilder();
        for (AvailableVisit visit : visits) {
            builder.append(String.format("%s, %s, %s, %s", visit.getDateTime().format(DATE_TIME_FORMATTER), visit.getService(), visit.getClinic(), visit.getDoctor()));
            builder.append("\n");
        }
        return builder;
    }
}
