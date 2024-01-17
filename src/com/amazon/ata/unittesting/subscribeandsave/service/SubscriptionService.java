package com.amazon.ata.unittesting.subscribeandsave.service;

import com.amazon.ata.resources.debugging.classroom.dependencies.AmazonIdentityService;
import com.amazon.ata.resources.debugging.classroom.dependencies.AmazonProductService;
import com.amazon.ata.resources.debugging.classroom.dependencies.Product;
import com.amazon.ata.unittesting.subscribeandsave.dao.SubscriptionDAO;
import com.amazon.ata.unittesting.subscribeandsave.types.Subscription;

import org.apache.commons.lang3.StringUtils;

/**
 * Subscribe and service API. Currently supports creating subscriptions and fetching them. Subscriptions can
 * only be made for valid amazon products by valid amazon customers. Subscriptions are persisted by the SubscriptionDAO.
 */
public class SubscriptionService {

    private AmazonIdentityService identityService;
    private AmazonProductService productService;
    private SubscriptionDAO subscriptionDAO;

    /**
     * Creates new subscription service instance with the given dependencies.
     *
     * @param identityService The identity service to use for validating customers
     * @param subscriptionDAO The subscription DAO for reading/writing subscriptions
     * @param productService  The product service to use for validating/getting products
     */
    public SubscriptionService(AmazonIdentityService identityService,
                               SubscriptionDAO subscriptionDAO,
                               AmazonProductService productService) {
        this.identityService = identityService;
        this.subscriptionDAO = subscriptionDAO;
        this.productService = productService;
    }

    /**
     * Creates a new subscription for given customer and ASIN for the given frequency (given in months between
     * deliveries).
     * <p>
     * Throws {@code IllegalArgumentException} if customer ID is blank/invalid, ASIN is blank/invalid, ASIN
     * is unsubscribable, or if frequency is invalid (less than 1 or greater than 6).
     *
     * @param customerId The customer's ID
     * @param asin       The ASIN of the product to subscribe customer to
     * @param frequency  The frequency of delivery (delivery every N months)
     * @return the new {@code Subscription} if successful, {@code null} otherwise
     */
    public Subscription subscribe(String customerId, String asin, int frequency) {
        if (StringUtils.isBlank(customerId) || StringUtils.isBlank(asin)) {
            throw new IllegalArgumentException(
                String.format(
                    "Invalid inputs. A Customer ID and ASIN must be provided. Provided: {Customer ID: %s, ASIN: %s}",
                    customerId,
                    asin)
            );
        }

        if (frequency < 1 || frequency > 6) {
            throw new IllegalArgumentException(
                String.format(
                    "Invalid frequency value. Please provide how often (in months) the " +
                        "subscription should occur - between 1 and 6. Provided: {Frequency: %d}",
                    frequency));
        }

        if (!identityService.validateCustomer(customerId)) {
            throw new IllegalArgumentException(
                String.format("Unable to create subscription for customerId: %s. Unknown customer.", customerId)
            );
        }

        Product product = productService.getProductByAsin(asin);
        if (product == null) {
            throw new IllegalArgumentException(
                String.format("Unable to create subscription for ASIN: %s. Unrecognized ASIN.", asin)
            );
        }

        if (!product.isSNS()) {
            throw new IllegalArgumentException(
                String.format("Cannot create a subscription for an unsubscribable ASIN: %s", asin)
            );
        }

        return subscriptionDAO.createSubscription(customerId, asin, frequency);
    }

    /**
     * Returns the {@code Subscription} corresponding to the given subscription ID.
     *
     * @param subscriptionId The ID of the subscription to fetch
     * @return the {@code Subscription} if one is found, {@code null} otherwise
     */
    public Subscription getSubscription(String subscriptionId) {
        if (StringUtils.isBlank(subscriptionId)) {
            throw new IllegalArgumentException("A subscriptionId must be provided.");
        }

        return subscriptionDAO.getSubscription(subscriptionId);
    }
}
