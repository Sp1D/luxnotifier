package sp1d.luxnotifier.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sp1d.luxnotifier.dao.SubscriptionDao;
import sp1d.luxnotifier.dao.UserDao;
import sp1d.luxnotifier.entity.Subscription;
import sp1d.luxnotifier.entity.User;
import sp1d.luxnotifier.request.LoginRequestSender;
import sp1d.luxnotifier.request.SearchPageRequestSender;
import sp1d.luxnotifier.request.SearchTimeslotRequestSender;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sp1d.luxnotifier.entity.Subscription.aSubscription;
import static sp1d.luxnotifier.entity.User.anUser;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledVisitFinderTest {

    @Mock
    private UserNotifier notifier;
    @Mock
    private VisitBooker booker;
    @Mock
    private UserDao userDao;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private LoginRequestSender loginRequestSender;
    @Mock
    private SearchPageRequestSender searchPageRequestSender;
    @Mock
    private SearchTimeslotRequestSender searchTimeslotRequestSender;
    @InjectMocks
    private ScheduledVisitFinder finder;

    @Before
    public void setUp() throws Exception {
        when(searchPageRequestSender.send()).thenReturn("");
        when(searchTimeslotRequestSender.send(anyMap())).thenReturn("");
    }

    @Test
    public void doesNothingIfNoSubscriptionsAvailable() {
        when(userDao.findAll()).thenReturn(userWithoutSubscriptions());
        finder.find();
        verify(loginRequestSender, never()).send(any());
        verify(notifier, never()).notifyUser(any(), Collections.emptyList());
        verify(booker, never()).bookVisit(any());
    }

    @Test
    public void loginsUserIfThereIsAtLeastOneSubscription() {
        User user = userWithOneSubscription();
        when(userDao.findAll()).thenReturn(Collections.singletonList(user));
        finder.find();
        verify(loginRequestSender, atLeastOnce()).send(user.asMap());
    }

    private User userWithOneSubscription() {
        User user = anUser()
                .withEmail("test@test.com")
                .withPassword("pass")
                .build();
        Subscription subscription = aSubscription()
                .withUserEmail(user.getEmail())
                .withServiceId("42")
                .build();
        when(subscriptionDao.findByUserEmail(user.getEmail()))
                .thenReturn(Collections.singletonList(subscription));
        return user;
    }

    private List<User> userWithoutSubscriptions() {
        return Collections.singletonList(
                anUser()
                        .withEmail("test@test.com")
                        .withPassword("pass")
                        .build()
        );
    }
}