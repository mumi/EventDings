package org.av360.maverick.eventdispatcher.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;


public class EventdingsDeliveryApplication {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(EventdingsDeliveryApplication.class);

        log.info("Starting EventDings Delivery application");

        log.info("Starting Subscription Refresh Task");
        Timer timer = new Timer();
        timer.schedule(new RefreshSubscriptionsTask(), 0, 5 * 60 * 1000);
    }
}