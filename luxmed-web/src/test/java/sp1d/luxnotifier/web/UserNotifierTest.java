package sp1d.luxnotifier.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import sp1d.luxnotifier.entity.Subscription;
import sp1d.luxnotifier.parser.AvailableVisit;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserNotifierTest {
    private static final String USER_EMAIL = "test@test.com";
    @Captor
    private ArgumentCaptor<SimpleMailMessage> msgCaptor;
    @Mock
    private MailSender mailSender;
    @InjectMocks
    private UserNotifier notifier;

    @Test
    public void sendsEmailToUsersAddress() {
        assertThat(notifyUserAndGetMessage().getTo()).contains(USER_EMAIL);
    }

    @Test
    public void sendsEmailFromLuxnotifierSp1DFtpSh() {
        assertThat(notifyUserAndGetMessage().getFrom()).contains("luxmed.notifier@gmail.com");
    }

    @Test
    public void sendsNonEmptyMessageBody() {
        assertThat(notifyUserAndGetMessage().getText()).isNotEmpty();
    }

    @Test
    public void sendsSubjectThatBeginsWithLuxnotifier() {
        assertThat(notifyUserAndGetMessage().getSubject()).startsWith("Luxnotifier");
    }

    @Test
    public void sendsAllVisitsInMessageBody() {
        assertThat(notifyUserAndGetMessage().getText())
                .contains(
                        "23-02-2018 07:40, Doctor Who, Elm street, Batman",
                        "24-02-2018 15:20, Doctor Who, Elm street, Batman"
                );
    }

    @Test
    public void sendsMessageBodyInCorrectFormat() {
        assertThat(notifyUserAndGetMessage().getText()).contains(
                "New visits are available:\n" +
                        "23-02-2018 07:40, Doctor Who, Elm street, Batman\n" +
                        "24-02-2018 15:20, Doctor Who, Elm street, Batman\n\n" +
                        "Please proceed to Luxmed site in order to book a visit."
        );
    }

    @Test
    public void writesFirstVisitsDateTimeAndServiceNameToEmailSubject() {
        assertThat(notifyUserAndGetMessage().getSubject()).contains("23-02-2018 07:40, Doctor Who");
    }

    private SimpleMailMessage notifyUserAndGetMessage() {
        notifier.notifyUser(aSubscription(), visits());
        verify(mailSender, only()).send(msgCaptor.capture());
        return msgCaptor.getValue();
    }

    private List<AvailableVisit> visits() {
        return Collections.unmodifiableList(Arrays.asList(
                AvailableVisit.anAvailableVisit()
                        .withDoctor("Batman")
                        .withDateTime(LocalDateTime.of(2018, 2, 23, 7, 40))
                        .withClinic("Elm street")
                        .withService("Doctor Who")
                        .build(),
                AvailableVisit.anAvailableVisit()
                        .withDoctor("Batman")
                        .withDateTime(LocalDateTime.of(2018, 2, 24, 15, 20))
                        .withClinic("Elm street")
                        .withService("Doctor Who")
                        .build()
        ));
    }

    private Subscription aSubscription() {
        return Subscription.aSubscription()
                .withUserEmail(USER_EMAIL)
                .withServiceId("1")
                .withServiceName("Doctor Who")
                .withLanguageId("42")
                .withLanguageName("Kiswahili")
                .build();
    }
}