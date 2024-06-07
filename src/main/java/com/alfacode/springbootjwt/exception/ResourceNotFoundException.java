package com.alfacode.springbootjwt.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;

    private final String resourceField;

    private final String resourceValue;

    public ResourceNotFoundException(String resourceName, String resourceField, String resourceValue) {
        super(String.format("%s with %s %s is not found", resourceName, resourceField, resourceValue));
        this.resourceName = resourceName;
        this.resourceField = resourceField;
        this.resourceValue = resourceValue;
    }

}
