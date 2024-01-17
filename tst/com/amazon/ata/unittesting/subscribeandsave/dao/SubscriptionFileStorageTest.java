package com.amazon.ata.unittesting.subscribeandsave.dao;


import com.amazon.ata.unittesting.subscribeandsave.test.util.SubscriptionRestorer;
import com.amazon.ata.unittesting.subscribeandsave.types.Subscription;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubscriptionFileStorageTest {

    private static final String TEST_FILE_PATH = "resources/unittesting/classroom/subscribeandsave/subscriptions.csv";
    private static final String ASIN = "B01BMDAVIY";
    private static final String CUSTOMER_ID = "amzn1.account.AEZI3A063427738YROOFT8WCXKDE";

    private SubscriptionFileStorage subscriptionFileStorage;

    @BeforeEach
    private void setupSubscriptionFileStorage() {
        subscriptionFileStorage = new SubscriptionFileStorage(new File(TEST_FILE_PATH));
    }


    @Test
    void writeSubscription_newSubscription_subscriptionReturnedWithId() {
        // GIVEN - a new subscription to save
        int frequency = 1;
        Subscription newSubscription = Subscription.builder()
                                                   .withAsin(ASIN)
                                                   .withCustomerId(CUSTOMER_ID)
                                                   .withFrequency(frequency)
                                                   .build();

        // WHEN - create a new subscription
        Subscription result = subscriptionFileStorage.createSubscription(newSubscription);

        // THEN
        // a subscription should be returned
        assertNotNull(result, "Writing subscription should return the subscription");
        // the id field should be populated
        assertNotNull(result.getId(), "Writing subscription should return a subscription with an id field");
        // the customer ID should match
        assertEquals(CUSTOMER_ID, result.getCustomerId(),
                     "Writing a subscription should return a subscription with matching customer ID");
        // the ASIN should match
        assertEquals(ASIN, result.getAsin(),
                     "Writing a subscription should return a subscription with matching ASIN");
        // the frequency should match
        assertEquals(frequency, result.getFrequency(),
                     "Writing a subscription should return a subscription with matching frequency");
    }

    @Test
    void writeSubscription_subsequentGetById_returnsCorrectFields() {
        // GIVEN - a new subscription to save
        int frequency = 1;
        Subscription newSubscription = Subscription.builder()
                                                   .withAsin(ASIN)
                                                   .withCustomerId(CUSTOMER_ID)
                                                   .withFrequency(frequency)
                                                   .build();

        // WHEN - create a new subscription
        Subscription result = subscriptionFileStorage.createSubscription(newSubscription);

        // THEN
        // subsequently fetching the subscription
        Subscription refreshedSubscription = subscriptionFileStorage.getSubscriptionById(result.getId());
        // the customer ID should match
        assertEquals(CUSTOMER_ID, refreshedSubscription.getCustomerId(),
                     "Reading a subscription after writing should result in matching customer ID");
        // the ASIN should match
        assertEquals(ASIN, refreshedSubscription.getAsin(),
                     "Reading a subscription after writing should result in matching customer ID");
        // the frequency should match
        assertEquals(frequency, refreshedSubscription.getFrequency(),
                     "Reading a subscription after writing should result in matching frequency");
    }

    @Test
    void getSubscriptionById_withExistingSubscriptionId_returnsCorrectFields() {
        // GIVEN
        // An existing Subscription, with its expected ASIN, customer ID,
        String subscriptionId = "81a9792e-9b4c-4090-aac8-28e733ac2f54";
        String expectedAsin = "B00006IEJB";
        String expectedCustomerId = "amzn1.account.AEZI3A027560538W420H09ACTDP2";
        int expectedFrequency = 3;

        // WHEN - Get the Subscription by ID
        Subscription result = subscriptionFileStorage.getSubscriptionById(subscriptionId);

        // THEN
        // the subscription ID should match
        assertEquals(subscriptionId, result.getId(),
                     "Getting a subscription should return a subscription with same subscription ID");
        // the customer ID should match
        assertEquals(expectedCustomerId, result.getCustomerId(),
                     "Getting a subscription should return a subscription with correct customer ID");
        // the ASIN should match
        assertEquals(expectedAsin, result.getAsin(),
                     "Getting a subscription should return a subscription with correct ASIN");
        // the frequency should match
        assertEquals(expectedFrequency, result.getFrequency(),
                     "Getting a subscription should return a subscription with correct frequency");
    }

    @Test
    void updateSubscription_withExistingSubscriptionAndUpdatedFrequency_returnsUpdatedFrequency() {
        // GIVEN - An existing subscription with frequency 1, updated to frequency 2
        int newFrequency = 2;
        Subscription updatedSubscription = Subscription.builder()
                                                       .withSubscriptionId("1fe240f4-3296-4827-8c0e-7fa571b6f49f")
                                                       .withCustomerId("amzn1.account.AEZI3A09486461G3DRR0VQPQHQ9I")
                                                       .withAsin("B01BMDAVIY")
                                                       .withFrequency(newFrequency)
                                                       .build();

        // WHEN - Update the subscription
        Subscription result = subscriptionFileStorage.updateSubscription(updatedSubscription);

        // THEN - returned subscription frequency matches new value
        assertEquals(newFrequency, result.getFrequency());
    }


    // PARTICIPANTS: Remove this line and add your updateSubscription() tests here.


    @BeforeEach
    @AfterEach
    private void restoreSubscriptions() {
        SubscriptionRestorer.restoreSubscriptions();
    }
}
