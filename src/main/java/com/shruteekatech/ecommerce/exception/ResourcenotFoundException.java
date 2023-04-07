package com.shruteekatech.ecommerce.exception;

public class ResourcenotFoundException extends RuntimeException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    String resourceName;
    String fieldName;
    long fieldValue;
    public ResourcenotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
