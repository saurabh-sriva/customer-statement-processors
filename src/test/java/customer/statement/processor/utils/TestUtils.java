package customer.statement.processor.utils;

import java.io.InputStream;

public class TestUtils {
    public static InputStream readFile(final String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}
