package tool.utils;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

/**
 * Util class. Used for getting char stream from file.
 */
public class CharStreamUtils {

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
