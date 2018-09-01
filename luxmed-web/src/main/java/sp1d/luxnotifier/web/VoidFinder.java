package sp1d.luxnotifier.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("noconnection")
@Component
public class VoidFinder {
    private static final Logger LOG = LoggerFactory.getLogger(VoidFinder.class);

    @Scheduled(cron = "0 */15 * * * *")
    public void find() {
        LOG.info("Spring profile \"noconnection\" is active, so we won't run actual visits search.");
    }


}
