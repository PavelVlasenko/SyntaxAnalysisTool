package tool.utils;

import guru.nidi.graphviz.engine.Format;

/**
 * Resolves file suffix that depends on format
 */
public class FormatResolver {

    public static String resolve(Format format) {
        switch (format) {
            case PNG: return ".png";
            case XDOT: return ".dot";
            case SVG: return ".svg";
            case SVG_STANDALONE: return ".svg";
            case PLAIN: return ".plain";
            case PS: return ".ps";
            case JSON: return ".json";
            default: throw new RuntimeException("Unknown exception");
        }
    }
}
