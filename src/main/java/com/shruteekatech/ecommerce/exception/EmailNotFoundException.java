package com.shruteekatech.ecommerce.exception;

public class EmailNotFoundException extends RuntimeException{

    String resourceName;
    String fieldName;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public EmailNotFoundException(String message, String resourceName, String fieldName) {
        super(String.format("%s not found with %s",resourceName,fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }
}
