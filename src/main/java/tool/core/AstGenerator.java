package tool.core;

import org.antlr.v4.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.Python3Lexer;
import tool.antlr4.Python3Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AstGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstGenerator.class);

    public void generateAst() {
        LOGGER.info("Generate AST.");
        CharStream input = null;
        try {
        ClassLoader classLoader = getClass().getClassLoader();
        Path path = Paths.get(classLoader.getResource("examples/python_example.py").toURI());


            input = CharStreams.fromPath(path);
        } catch (Exception e) {
            LOGGER.error("Error while parsing file.");
        }
        Python3Lexer lexer = new Python3Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        ParserRuleContext tree = parser.stmt();
    }
}
