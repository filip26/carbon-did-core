package com.apicatalog.did.adapter;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;

class MapEntryAdapter {

    public static String string(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof String value) {
            return value;
        }
        throw new IllegalArgumentException(
                "Property '" + entry.getKey() + "' must be a string.");
    }

    public static Instant instant(Map.Entry<String, Object> entry) {
        try {
            return Instant.parse(string(entry));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Property '" + entry.getKey() + "' must be an ISO-8601 instant.",
                    e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> object(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof Map<?, ?> value) {
            return (Map<String, Object>) value;
        }
        throw new IllegalArgumentException(
                "Property '" + entry.getKey() + "' must be a JSON object.");
    }

    public static DidUrl didUrl(Map.Entry<String, Object> entry) {
        var value = string(entry);
        return DidUrl.parse(value);
    }

    public static Did did(Map.Entry<String, Object> entry) {
        var value = string(entry);
        return Did.parse(value);
    }

    public static String url(Map.Entry<String, Object> entry) {
        var value = string(entry);

//        try {
//            return new URI(value);
//        } catch (URISyntaxException e) {
//            throw new IllegalArgumentException(
//                    "Property '" + entry.getKey()
//                            + "' must be a valid URL.",
//                    e);
//        }

        // just a simple validation
        if (!startsWithScheme(value)) {
            throw new IllegalArgumentException(
                    "Property '" + entry.getKey() + "' must be a valid URL.");
        }

        return value;
    }

    public static Collection<String> stringCollection(Map.Entry<String, Object> entry) {

        if (entry.getValue() == null) {
            return List.of();
        }
        
        if (entry.getValue() instanceof String value) {
            return List.of(value);
        }

        if (entry.getValue() instanceof Collection<?> values) {
            var result = new ArrayList<String>(values.size());

            for (var value : values) {
                if (!(value instanceof String string)) {
                    throw new IllegalArgumentException(
                            "Property '" + entry.getKey()
                                    + "' must contain only strings.");
                }
                result.add(string);
            }

            return result;
        }

        throw new IllegalArgumentException(
                "Property '" + entry.getKey()
                        + "' must be a string or collection of strings.");
    }

    public static Collection<Object> toCollection(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof Collection<?> values) {
            return List.copyOf(values);

        } else if (entry.getValue() != null) {
            return List.of(entry.getValue());
        }
        return List.of();
    }

    private static final boolean startsWithScheme(final String uri) {

        if (uri == null
                || uri.length() < 2 // a scheme must have at least one letter followed by ':'
                || !Character.isLetter(uri.codePointAt(0)) // a scheme name must start with a letter
        ) {
            return false;
        }

        for (int i = 1; i < uri.length(); i++) {

            if (
            // a scheme name must start with a letter followed by a letter/digit/+/-/.
            Character.isLetterOrDigit(uri.codePointAt(i))
                    || uri.charAt(i) == '-' || uri.charAt(i) == '+' || uri.charAt(i) == '.') {
                continue;
            }

            // a scheme name must be terminated by ':'
            return uri.charAt(i) == ':';
        }
        return false;
    }

}
