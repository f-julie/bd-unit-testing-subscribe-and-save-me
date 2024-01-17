package com.amazon.ata.unittesting.subscribeandsave;

import com.amazon.ata.resources.debugging.classroom.dependencies.AmazonIdentityService;
import com.amazon.ata.resources.debugging.classroom.dependencies.AmazonProductService;
import com.amazon.ata.unittesting.subscribeandsave.dao.SubscriptionDAO;
import com.amazon.ata.unittesting.subscribeandsave.dao.SubscriptionFileStorage;

import java.nio.file.Paths;

/**
 * Provides inversion of control for the SNS MLP by instantiating all of the
 * dependencies needed by the SubscriptionDebugUtil and its dependency classes.
 */
public class App {
    public static final String DATA_FILE_ROOT_PATH =
        Paths.get("resources", "unittesting", "classroom", "subscribeandsave").toString();

    /**
     * Returns a product service.
     *
     * @return Product service usable for fetching products by ASIN
     */
    public static AmazonProductService getAmazonProductService() {
        return new AmazonProductService(Paths.get(DATA_FILE_ROOT_PATH, "catalog.json").toFile());
    }

    /**
     * Returns an identity service.
     *
     * @return Identity service usable for fetching customers by ID
     */
    public static AmazonIdentityService getAmazonIdentityService() {
        return new AmazonIdentityService(Paths.get(DATA_FILE_ROOT_PATH, "customers.txt").toFile());
    }

    /**
     * Returns a subscription DAO.
     *
     * @return A subscription DAO for reading/writing subscriptions
     */
    public static SubscriptionDAO getSubscriptionDAO() {
        return new SubscriptionDAO(getSubscriptionFileStorage());
    }

    /**
     * Returns a subscription file storage manager.
     *
     * @return A subscription file data store
     */
    public static SubscriptionFileStorage getSubscriptionFileStorage() {
        return new SubscriptionFileStorage(Paths.get(DATA_FILE_ROOT_PATH, "subscriptions.csv").toFile());
    }
}
