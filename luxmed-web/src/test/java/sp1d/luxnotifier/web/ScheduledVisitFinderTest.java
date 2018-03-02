package sp1d.luxnotifier.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sp1d.luxnotifier.dao.NotificationDao;
import sp1d.luxnotifier.dao.SubscriptionDao;
import sp1d.luxnotifier.dao.UserDao;
import sp1d.luxnotifier.entity.Subscription;
import sp1d.luxnotifier.entity.User;
import sp1d.luxnotifier.parser.AvailableVisit;
import sp1d.luxnotifier.parser.AvailableVisitsParser;
import sp1d.luxnotifier.parser.AvailableVisitsParserFactory;
import sp1d.luxnotifier.parser.SimpleParser;
import sp1d.luxnotifier.request.LoginRequestSender;
import sp1d.luxnotifier.request.SearchPageRequestSender;
import sp1d.luxnotifier.request.SearchTimeslotRequestSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sp1d.luxnotifier.entity.Subscription.aSubscription;
import static sp1d.luxnotifier.entity.User.anUser;
import static sp1d.luxnotifier.parser.AvailableVisit.anAvailableVisit;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledVisitFinderTest {

    @Mock
    private UserNotifier notifier;
    @Mock
    private UserDao userDao;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private NotificationDao notificationDao;
    @Mock
    private LoginRequestSender loginRequestSender;
    @Mock
    private SearchPageRequestSender searchPageRequestSender;
    @Mock
    private SearchTimeslotRequestSender searchTimeslotRequestSender;
    @Mock
    private AvailableVisitsParserFactory availableVisitsParserFactory;
    @Mock
    private SimpleParser simpleParser;
    @InjectMocks
    private ScheduledVisitFinder finder;

    @Before
    public void setUp() throws Exception {
        when(searchPageRequestSender.send()).thenReturn("");
        when(searchTimeslotRequestSender.send(anyMap())).thenReturn("");
        when(availableVisitsParserFactory.createParser(any())).thenReturn(AvailableVisitsParser.anAvailableVisitsParser(""));
        when(simpleParser.parseVerificationToken(anyString())).thenReturn("token");
    }

    @Test
    public void doesNothingIfNoSubscriptionsAvailable() {
        when(userDao.findAll()).thenReturn(userWithoutSubscriptions());

        finder.find();

        verify(loginRequestSender, never()).send(any());
        verify(notifier, never()).notifyUser(any(), anyList());
    }

    @Test
    public void loginsUserIfThereIsAtLeastOneSubscription() {
        givenUserWithOneSubscription();

        finder.find();

        verify(loginRequestSender, atLeastOnce()).send(anyMap());
    }

    @Test
    public void savesNotifiedVisitsAfterNotification() {
        User user = givenUserWithOneSubscription();
        List<AvailableVisit> availableVisits = Arrays.asList(
                anAvailableVisit()
                        .withService("Service")
                        .withDoctor("Doctor")
                        .withClinic("Clinic")
                        .withDateTime(LocalDateTime.MIN)
                        .build(),
                anAvailableVisit()
                        .withService("newService")
                        .withDoctor("newDoctor")
                        .withClinic("newClinic")
                        .withDateTime(LocalDateTime.MIN)
                        .build()
        );
        givenAvailableVisits(availableVisits);

        finder.find();

        InOrder inOrder = inOrder(notifier, notificationDao);
        inOrder.verify(notifier).notifyUser(any(), any());
        inOrder.verify(notificationDao).saveNotifiedVisits(eq(user.getEmail()), eq(availableVisits));
    }

    @Test
    public void sendsNotificationOnlyAboutNewVisits() {
        givenUserWithOneSubscription();
        List<AvailableVisit> availableVisits = new ArrayList<>();
        availableVisits.add(anAvailableVisit()
                .withService("Service")
                .withDoctor("Doctor")
                .withClinic("Clinic")
                .withDateTime(LocalDateTime.MIN)
                .build());
        availableVisits.add(anAvailableVisit()
                .withService("newService")
                .withDoctor("newDoctor")
                .withClinic("newClinic")
                .withDateTime(LocalDateTime.MIN)
                .build());
        givenAvailableVisits(availableVisits);
        when(notificationDao.loadNotifiedVisits(any())).thenReturn(Collections.singletonList(
                anAvailableVisit()
                        .withService("Service")
                        .withDoctor("Doctor")
                        .withClinic("Clinic")
                        .withDateTime(LocalDateTime.MIN)
                        .build()
        ));

        finder.find();

        verify(notifier).notifyUser(any(), eq(Collections.singletonList(
                anAvailableVisit()
                        .withService("newService")
                        .withDoctor("newDoctor")
                        .withClinic("newClinic")
                        .withDateTime(LocalDateTime.MIN)
                        .build()
        )));
    }

    private User givenUserWithOneSubscription() {
        User user = userWithOneSubscription();
        when(userDao.findAll()).thenReturn(Collections.singletonList(user));
        return user;
    }

    private List<AvailableVisit> givenAvailableVisits(List<AvailableVisit> availableVisits) {
        AvailableVisitsParser availableVisitsParser = mock(AvailableVisitsParser.class);
        when(availableVisitsParser.parse()).thenReturn(availableVisits);
        when(availableVisitsParserFactory.createParser(any())).thenReturn(availableVisitsParser);
        return availableVisits;
    }

    private User userWithOneSubscription() {
        User user = anUser()
                .withEmail("test@test.com")
                .build();
        Subscription subscription = aSubscription()
                .withUserEmail(user.getEmail())
                .withServiceId("42")
                .withServiceName("Doctor Who")
                .withLanguageId("42")
                .withLanguageName("Mandarin")
                .withSearchUntilDate(LocalDate.of(2018, 2, 14))
                .build();
        when(subscriptionDao.findByUserEmail(user.getEmail()))
                .thenReturn(Collections.singletonList(subscription));
        return user;
    }

    private List<User> userWithoutSubscriptions() {
        return Collections.singletonList(
                anUser()
                        .withEmail("test@test.com")
                        .build()
        );
    }
}