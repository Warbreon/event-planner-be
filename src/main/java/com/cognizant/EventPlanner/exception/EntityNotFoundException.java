package com.cognizant.EventPlanner.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entityType, Object id) {
        super(entityType.getSimpleName() + " not found with id: " + id);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}
