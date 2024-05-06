package com.cognizant.EventPlanner.util;

import java.util.function.Consumer;

public class FieldUtils {
    public static <T> void updateField(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}