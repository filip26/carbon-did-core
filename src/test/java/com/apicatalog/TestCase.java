package com.apicatalog;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

public class TestCase {

//    public static final JsonDocument resource(String name) throws IOException, URISyntaxException {
//        try (JsonReader reader = Json.createReader(TestCase.class.getResourceAsStream(name))) {
//            return JsonDocument.of(reader.readObject());
//        }
//    }

    public static final Stream<Object[]> resources(String folder, String suffix) throws IOException, URISyntaxException {
        return Files.walk(Paths.get(TestCase.class.getResource(folder).toURI()), 1)
                .filter(name -> name.toString().endsWith(suffix))
                .sorted()
                .map(path -> {
                    try (JsonReader reader = Json.createReader(TestCase.class.getResourceAsStream(folder + "/" + path.getFileName().toString()))) {
                        return new Object[] { path.getFileName().toString(), reader.read() };
                    }
                });
    }

//    public static final boolean compareJson(final String testCase, final JsonValue data, final JsonStructure result, final JsonStructure expected) {
//
//        if (JsonLdComparison.equals(expected, result)) {
//            return true;
//        }
//
//        write(testCase, result, expected, null);
////FIXME
//        if (data != null) {
//            final StringWriter stringWriter = new StringWriter();
////            (new DictionaryWriter(new PrintWriter(stringWriter))).print(data);
//            System.out.print(stringWriter.toString());
//        }
//        fail("Expected " + expected + ", but was" + result);
//        return false;
//    }

    static void write(final String testCase, final JsonStructure result, final JsonStructure expected, Exception error) {
        final StringWriter stringWriter = new StringWriter();

        try (final PrintWriter writer = new PrintWriter(stringWriter)) {
            writer.println("Test " + testCase);

            final JsonWriterFactory writerFactory = Json.createWriterFactory(Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true));

            if (expected != null) {
                write(writer, writerFactory, "Expected", expected);
                writer.println();

//            } else if (testCase.expectErrorCode != null) {
//                writer.println("Expected: " + testCase.expectErrorCode);
            }

            if (result != null) {
                write(writer, writerFactory, "Actual", result);
                writer.println();
            }
            if (error != null) {
                writer.println("Actual: ");
                error.printStackTrace(writer);
            }
        }

        System.out.println(stringWriter.toString());
    }

    static final void write(final PrintWriter writer, final JsonWriterFactory writerFactory, final String name, final JsonValue result) {

        writer.println(name + ":");

        final StringWriter out = new StringWriter();

        try (final JsonWriter jsonWriter = writerFactory.createWriter(out)) {
            jsonWriter.write(result);
        }

        writer.write(out.toString());
        writer.println();
    }
}
