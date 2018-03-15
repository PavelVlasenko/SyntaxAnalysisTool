package tool.utils;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

public class CharStreamUtils {

    /**
     * Used for tests
     */
   /* private CharStream getStream(String fileName) {
        CharStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Path path = Paths.get(classLoader.getResource(fileName).toURI());
            input = CharStreams.fromPath(path);
        } catch (Exception e) {
            LOGGER.error("Error while parsing file.");
        }
        return input;
    }*/

    public static CharStream getStream(String fileName) {
        CharStream input = null;
        try {
            input = CharStreams.fromFileName(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}
