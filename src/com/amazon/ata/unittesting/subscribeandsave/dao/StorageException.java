package com.amazon.ata.unittesting.subscribeandsave.dao;

/**
 * Exception thrown when unable to properly interact with a storage resource.
 */
public class StorageException extends RuntimeException {

    private static final long serialVersionUID = -2946082998157301400L;

    /**
     * Creates a {@code StorageException} with specified message.
     *
     * @param message The error message
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Creates a {@code StorageException} with specified message and caused-by exception.
     *
     * @param message The error message
     * @param t       The original cause of the exception
     */
    public StorageException(String message, Throwable t) {
        super(message, t);
    }
}
