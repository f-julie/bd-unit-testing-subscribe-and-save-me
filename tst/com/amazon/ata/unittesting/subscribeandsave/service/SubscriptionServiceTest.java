package com.amazon.ata.unittesting.subscribeandsave.service;

import com.amazon.ata.unittesting.subscribeandsave.App;
import com.amazon.ata.unittesting.subscribeandsave.dao.StorageException;
import com.amazon.ata.unittesting.subscribeandsave.test.util.SubscriptionRestorer;
import com.amazon.ata.unittesting.subscribeandsave.types.Subscription;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class SubscriptionServiceTest {
    private SubscriptionService subscriptionService;

    @BeforeEach
    private void setupServiceUnderTest() {
        subscriptionService = new SubscriptionService(App.getAmazonIdentityService(),
                                                      App.getSubscriptionDAO(),
                                                      App.getAmazonProductService());
    }

    // getSubscription():

    @Test
    void getSubscription_existingSubscription_subscriptionReturned() {
        // GIVEN - a valid subscription
        String customerId = "amzn1.account.AEZI3A063427738YROOFT8WCXKDE";
        String asin = "B00006IEJB";
        int frequency = 2;
        Subscription newSubscription = subscriptionService.subscribe(customerId, asin, frequency);
        String subscriptionId = newSubscription.getId();

        // WHEN - get the corresponding subscription
        Subscription result = subscriptionService.getSubscription(subscriptionId);

        // THEN
        // a subscription object is returned, with matching id
        assertEquals(subscriptionId, result.getId(), "Expected result of getSubscription to have same ID");
        // with expected customer ID
        assertEquals(customerId, result.getCustomerId(), "Expected result of getSubscription customer ID to match");
        // with expected ASIN
        assertEquals(asin, result.getAsin(), "Expected result of getSubscription ASIN to match");
        // with expected frequency
        assertEquals(frequency, result.getFrequency(), "Expected result of getSubscription frequency to match");
    }

    @Test
    void getSubscription_unknownSubscription_returnsNull() {
        // GIVEN - an invalid subscription ID
        String subscriptionId = "Not a valid subscription ID";

        // WHEN - get the corresponding subscription
        Subscription result = subscriptionService.getSubscription(subscriptionId);

        // THEN - null returned
        assertNull(result, "Expected result of getting a non-existing subscription ID to be null");
    }

    // subscribe():

    @Test
    void subscribe_newSubscription_subscriptionReturned() {
        // GIVEN
        // A valid customerId to make a subscription for
        String customerId = "amzn1.account.AEZR3A02756837HDND93HDN93112";
        // A valid ASIN to subscribe to
        String asin = "B00ILBUEVK";
        // A valid frequency to receive subscription
        int frequency = 1;

        // WHEN - create a new subscription
        Subscription result = subscriptionService.subscribe(customerId, asin, frequency);

        // THEN
        // Subscription should be returned and the id field should be populated
        assertNotNull(result, "Creating new subscription should return non-null Subscription");
        assertNotNull(result.getId(), "Creating new subscription should return Subscription with ID");
        // Customer ID should match
        assertEquals(customerId, result.getCustomerId(), "Creating new subscription: customer ID should match");
        // ASIN should match
        assertEquals(asin, result.getAsin(), "Creating new subscription: ASIN should match");
        // Frequency should match
        assertEquals(frequency, result.getFrequency(), "Creating new subscription: frequency should match");
    }

    @Test
    void subscribe_unknownCustomer_exceptionOccurs() {
        // GIVEN
        // An invalid customerId to make a subscription for
        String customerId = "12345678";
        // Valid ASIN
        String asin = "B00006IEJB";
        // Valid frequency
        int frequency = 1;

        // WHEN Try to create a new subscription
        // THEN Throw IllegalArgumentException

        // PARTICIPANTS: Here is one way to just verify that an exception has been thrown.
        assertThrows(IllegalArgumentException.class,
                     () -> subscriptionService.subscribe(customerId, asin, frequency),
                     "Expected subscribing with invalid customer ID to result in exception");
    }

    @Test
    void subscribe_withUnknownAsin_throwsMeaningfulException() {
        // GIVEN
        // Valid customer
        String customerId = "amzn1.account.AEZI3A027560538W420H09ACTDP2";
        // Invalid ASIN
        String asin = "12345678";
        // Valid frequency
        int frequency = 1;

        // WHEN - Try to create a new subscription
        // THEN - Throw IllegalArgumentException

        // PARTICIPANTS: Here's a way to catch and inspect the exception itself to make sure it's what we expect to
        // have happen. A little more fragile (what if the implementer changes the error message?),
        // but can sometimes be useful.
        try {
            subscriptionService.subscribe(customerId, asin, frequency);
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().contains("ASIN")) {

                // PARTICIPANTS: You can test for your own failure condition, and explicitly call fail() to
                // indicate that a JUnit test has failed.
                fail(String.format(
                    "Unknown ASIN resulted in exception, without helpful message about invalid ASIN: %s", e)
                );
            }

            // test passed!
            return;
        }

        fail("Creating subscription with invalid ASIN failed to throw exception");
    }

    @Test
    void subscribe_withExistingUnsubscribableProduct_throwsException() {
        // GIVEN
        // Valid customer ID
        String customerId = "amzn1.account.AEZI3A027560538W420H09ACTDP2";
        // Valid product that is not Subscribable (isSNS is false)
        String asin = "B07R5QD598";
        // valid frequency
        int frequency = 1;

        // WHEN - Try to create a new subscription
        // THEN - Throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                     () -> subscriptionService.subscribe(customerId, asin, frequency),
                     "Expected subscribing to product that is not SNS enabled to throw exception"
        );
    }

    @Test
    void subscribe_withExistingSubscribedCustomerAsinPair_throwsException() {
        // GIVEN - a valid subscription
        String customerId = "amzn1.account.AEZI3A063427738YROOFT8WCXKDE";
        String asin = "B00006IEJB";
        int frequency = 2;
        subscriptionService.subscribe(customerId, asin, frequency);

        // WHEN - Try to create a duplicate subscription
        // THEN - Throw StorageException
        assertThrows(StorageException.class,
                     () -> subscriptionService.subscribe(customerId, asin, 3),
                     "Expected trying to create subscription for same customer/ASIN pair to throw exception"
        );
    }

    @BeforeEach
    @AfterEach
    private void restoreSubscriptions() {
        SubscriptionRestorer.restoreSubscriptions();
    }
}
