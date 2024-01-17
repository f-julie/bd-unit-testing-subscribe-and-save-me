package com.amazon.ata.unittesting.subscribeandsave.dao;

import com.amazon.ata.unittesting.subscribeandsave.types.Subscription;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Subscription data store that is file based.
 */
public class SubscriptionFileStorage {

    private File subscriptionsFile;

    /**
     * Creates a {@code SubscriptionFileStorage} using the specified file for reading/writing subscriptions.
     *
     * @param subscriptionsFile The subscription {@code File} to use
     */
    public SubscriptionFileStorage(File subscriptionsFile) {
        this.subscriptionsFile = subscriptionsFile;
    }

    /**
     * Creates a new subscription.
     * <p>
     * Throws {@code StorageException} if the subscription already exists or if an input/output error occurs.
     *
     * @param subscription the subscription to store
     * @return The subscription that was written
     */
    public Subscription createSubscription(Subscription subscription) {
        Subscription existingSubscription = getSubscription(subscription.getCustomerId(), subscription.getAsin());

        if (existingSubscription != null) {
            throw new StorageException(String.format(
                "Subscription already exists: %s. Please use updateSubscription()", existingSubscription));
        }

        String id = UUID.randomUUID().toString();
        subscription.setId(id);
        String subscriptionRecord = generateSubscriptionStorageRepresentation(subscription);

        try {
            FileUtils.writeStringToFile(subscriptionsFile, subscriptionRecord, Charset.defaultCharset(), true);
        } catch (IOException e) {
            throw new StorageException("Unable to save subscription.", e);
        }

        return subscription;
    }

    /**
     * Updates an existing subscription.
     * <p>
     * Throws {@code IllegalArgumentException} if the {@code Subscription} is null, missing an ID or if no
     * subscription is found for that ID.
     * <p>
     * Throws {@code StorageException} if an error occurs trying to write the updated record.
     *
     * @param subscription The {@code Subscription} to update (must already have a subscription ID)
     * @return the {@code Subscription} if writing succeeded
     */
    public Subscription updateSubscription(final Subscription subscription) {
        if (null == subscription) {
            throw new IllegalArgumentException("Subscription cannot be null");
        }
        if (null == subscription.getId()) {
            throw new IllegalArgumentException("Subscription's ID cannot be null");
        }

        Subscription[] existingSubscriptions = loadSubscriptions();
        boolean foundExistingSubscription = false;
        Subscription existingSubscription = null;

        for (int i = 0; i < existingSubscriptions.length; i++) {
            Subscription currSubscription = existingSubscriptions[i];

            if (subscription.getId().equals(currSubscription.getId())) {
                foundExistingSubscription = true;
                existingSubscription = subscription;
                break;
            }
        }

        if (!foundExistingSubscription) {
            throw new IllegalArgumentException("No subscription found for ID: " + subscription.getId());
        }

        String subscriptionRecords = generateSubscriptionRecords(existingSubscriptions);
        try {
            FileUtils.writeStringToFile(subscriptionsFile, subscriptionRecords, Charset.defaultCharset(), false);
        } catch (IOException e) {
            throw new StorageException("Unable to update subscription.", e);
        }

        return existingSubscription;
    }

    /**
     * Gets a {@code Subscription} by subscription ID.
     *
     * @param subscriptionId The subscription ID to look up
     * @return The {@code Subscription} if found, {@code null} otherwise
     */
    public Subscription getSubscriptionById(final String subscriptionId) {
        Subscription[] subscriptions = loadSubscriptions();

        for (Subscription subscription : subscriptions) {
            if (subscriptionId.equals(subscription.getId())) {
                return new Subscription(subscription);
            }
        }

        return null;
    }

    private Subscription getSubscription(final String customerId, final String asin) {
        Subscription[] subscriptions = loadSubscriptions();

        for (Subscription subscription : subscriptions) {
            if (customerId.equals(subscription.getCustomerId()) && asin.equals(subscription.getAsin())) {
                return new Subscription(subscription);
            }
        }

        return null;
    }

    private Subscription[] loadSubscriptions() {
        try {
            List<String> lines = FileUtils.readLines(subscriptionsFile, Charset.defaultCharset());

            List<Subscription> subscriptions = new ArrayList<>();
            for (String line : lines) {
                subscriptions.add(readSubscriptionStorageRepresentation(line));
            }

            return subscriptions.toArray(new Subscription[0]);
        } catch (IOException e) {
            throw new StorageException("Unable to access subscription data.", e);
        }
    }

    private String generateSubscriptionRecords(Subscription[] subscriptions) {
        StringBuffer sb = new StringBuffer();

        for (Subscription subscription : subscriptions) {
            sb.append(generateSubscriptionStorageRepresentation(subscription));
        }

        return sb.toString();
    }

    private String generateSubscriptionStorageRepresentation(final Subscription subscription) {
        return new StringBuilder(subscription.getId())
            .append(",")
            .append(subscription.getCustomerId())
            .append(",")
            .append(subscription.getAsin())
            .append(",")
            .append(subscription.getFrequency())
            .append("\n")
            .toString();
    }

    private Subscription readSubscriptionStorageRepresentation(final String subscriptionString) {
        String[] subscriptionData = subscriptionString.split("\\s*,\\s*");

        return Subscription.builder()
                           .withSubscriptionId(subscriptionData[0].trim())
                           .withCustomerId(subscriptionData[1])
                           .withAsin(subscriptionData[2])
                           .withFrequency(Integer.parseInt(subscriptionData[3].trim()))
                           .build();
    }
}
