package io.av360.eventdings.delivery;

import java.util.Timer;


public class EventdingsDeliveryApplication {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new RefreshSubscriptionsTask(), 0, 5 * 60 * 1000);
    }
}