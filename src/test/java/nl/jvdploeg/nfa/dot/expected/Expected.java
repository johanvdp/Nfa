package nl.jvdploeg.nfa.dot.expected;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import nl.jvdploeg.nfa.internal.testset.TestSet;

public class Expected {

    public static String getExpectedDfa(final TestSet testSet) throws IOException {
        return read(Expected.class.getResource("dfa" + testSet.getClass().getSimpleName() + ".dot"));
    }

    public static String getExpectedNfa(final TestSet testSet) throws IOException {
        return read(Expected.class.getResource("nfa" + testSet.getClass().getSimpleName() + ".dot"));
    }

    private static String read(final URL url) throws IOException {
        final URLConnection conn = url.openConnection();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
