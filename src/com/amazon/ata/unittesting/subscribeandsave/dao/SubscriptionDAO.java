package com.amazon.ata.unittesting.subscribeandsave.dao;

import com.amazon.ata.unittesting.subscribeandsave.types.Subscription;

/**
 * Provides read and write capability for Subscription data.
 */
public class SubscriptionDAO {

    private SubscriptionFileStorage storage;

    /**
     * Creates the DAO using the given storage manager.
     *
     * @param storage The object to manage subscription storage
     */
    public SubscriptionDAO(SubscriptionFileStorage storage) {
        this.storage = storage;
    }

    /**
     * Retrieves the {@code Subscription} for the given subscription ID.
     *
     * @param subscriptionId The ID to look up {@code Subscription} for
     * @return The {@code Subscription} if found, {@code null} otherwise
     */
    public Subscription getSubscription(String subscriptionId) {
        return storage.getSubscriptionById(subscriptionId);
    }

    /**
     * Creates a new subscription.
     *
     * @param customerId The customer who is subscribing
     * @param asin       The ASIN for the product customer is subscribing to
     * @param frequency  The frequency of th subscription (# of months between deliveries)
     * @return The newly created {@code Subscription} if successful, {@code null} otherwise
     */
    public Subscription createSubscription(String customerId, String asin, int frequency) {
        Subscription subscription = Subscription.builder()
                                                .withAsin(asin)
                                                .withCustomerId(customerId)
                                                .withFrequency(frequency)
                                                .build();

        return storage.createSubscription(subscription);
    }
}
