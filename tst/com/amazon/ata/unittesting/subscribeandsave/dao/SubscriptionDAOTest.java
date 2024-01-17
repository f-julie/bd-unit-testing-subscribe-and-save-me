package com.amazon.ata.unittesting.subscribeandsave.dao;

import com.amazon.ata.unittesting.subscribeandsave.App;
import com.amazon.ata.unittesting.subscribeandsave.test.util.SubscriptionRestorer;
import com.amazon.ata.unittesting.subscribeandsave.types.Subscription;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SubscriptionDAOTest {

    private static final String ASIN = "B01BMDAVIY";
    private static final String CUSTOMER_ID = "amzn1.account.AEZI3A06339413S37ZHKJQUEGLC4";
    private static final String SUBSCRIPTION_ID = "81a9792e-9b4c-4090-aac8-28e733ac2f54";

    private SubscriptionDAO subscriptionDao;

    @BeforeEach
    private void setupSubscriptionDao() {
        subscriptionDao = new SubscriptionDAO(App.getSubscriptionFileStorage());
    }


    @Test
    void getSubscription_existingSubscription_subscriptionReturned() {
        // GIVEN - a valid subscriptionId
        String subscriptionId = SUBSCRIPTION_ID;

        // WHEN - get the corresponding subscription
        Subscription result = subscriptionDao.getSubscription(subscriptionId);

        // THEN
        // a subscription object is returned
        assertNotNull(result);
        // with a matching id
        assertEquals(subscriptionId, result.getId(), "Getting a subscription should have matching ID");
    }

    @Test
    void getSubscription_subscriptionDoesNotExist_nullReturned() {
        // GIVEN - an invalid subscriptionId
        String subscriptionId = "123456789";

        // WHEN - get the corresponding subscription
        Subscription result = subscriptionDao.getSubscription(subscriptionId);

        // THEN - a subscription object is not returned, null is
        assertNull(result, "Getting a subscription for invalid ID should return null");
    }

    @Test
    void createSubscription_newSubscription_subscriptionReturned() {
        // GIVEN
        // a customerId to make a subscription for
        String customerId = CUSTOMER_ID;
        // asin to subscribe to
        String asin = ASIN;
        // and the frequency to receive
        int frequency = 1;

        // WHEN - create a new subscription
        Subscription result = subscriptionDao.createSubscription(customerId, asin, frequency);

        // THEN a subscription should be returned and the id field should be populated
        assertNotNull(result.getId(), "Creating a subscription should populate a subscription ID");
    }

    @BeforeEach
    @AfterEach
    private void restoreSubscriptions() {
        SubscriptionRestorer.restoreSubscriptions();
    }
}
