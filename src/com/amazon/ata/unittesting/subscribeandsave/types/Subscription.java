package com.amazon.ata.unittesting.subscribeandsave.types;

/**
 * A customer's product subscription. A customer subscribes to receive a product with a certain frequency. The frequency
 * is tracked in months, every X months the product is sent to the customer.
 */
public class Subscription {

    private String id;
    private String customerId;
    private String asin;
    // every x months
    private int frequency;

    /**
     * Copy construct a Subscription.
     *
     * @param other The {@code Subscription} to create a new copy of
     */
    public Subscription(final Subscription other) {
        this.id = other.getId();
        this.customerId = other.getCustomerId();
        this.asin = other.getAsin();
        this.frequency = other.getFrequency();
    }

    private Subscription() {
    }

    /**
     * Returns a new Subscription.Builder object for constructing a Subscription.
     *
     * @return new builder ready for constructing a Subscription
     */
    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAsin() {
        return asin;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{SubscriptionId: ").append(id);
        sb.append(", CustomerId: ").append(customerId);
        sb.append(", Asin: ").append(asin);
        sb.append(", Frequency: ").append(frequency);
        sb.append("}");

        return sb.toString();
    }

    /**
     * Builder for Subscriptions.
     */
    public static class Builder {
        private String subscriptionId;
        private String customerId;
        private String asin;
        private int frequency;

        /**
         * With subscription id builder.
         *
         * @param pSubscriptionId the subscription id
         * @return the builder
         */
        public Builder withSubscriptionId(String pSubscriptionId) {
            this.subscriptionId = pSubscriptionId;
            return this;
        }

        /**
         * With customer id builder.
         *
         * @param pCustomerId the customer id
         * @return the builder
         */
        public Builder withCustomerId(String pCustomerId) {
            this.customerId = pCustomerId;
            return this;
        }

        /**
         * With asin builder.
         *
         * @param pAsin the asin
         * @return the builder
         */
        public Builder withAsin(String pAsin) {
            this.asin = pAsin;
            return this;
        }

        /**
         * With frequency builder.
         *
         * @param pFrequency the frequency
         * @return the builder
         */
        public Builder withFrequency(int pFrequency) {
            this.frequency = pFrequency;
            return this;
        }

        /**
         * Builds the Order object from the current Builder state.
         *
         * @return constructed Order object
         */
        public Subscription build() {
            Subscription subscription = new Subscription();

            subscription.id = subscriptionId;
            subscription.customerId = customerId;
            subscription.asin = asin;
            subscription.frequency = frequency;

            return subscription;
        }
    }
}
