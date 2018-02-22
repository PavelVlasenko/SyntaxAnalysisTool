package tool.ast;

import org.antlr.v4.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AstGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstGenerator.class);

    public void generatePythonAst() {
        LOGGER.info("Generate Python AST.");
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

    public void generatePythonAst() {
        LOGGER.info("Generate Python AST.");
        CharStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Path path = Paths.get(classLoader.getResource("examples/python_example.py").toURI());


            input = CharStreams.fromPath(path);
        } catch (Exception e) {
            LOGGER.error("Error while parsing file.");
        }
        Python3sLexer lexer = new Python3sLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3sParser parser = new Python3sParser(tokens);

        ParserRuleContext tree = parser.
    }

    public void generateCAst() {
        LOGGER.info("Generate C AST.");
        CharStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Path path = Paths.get(classLoader.getResource("examples/c_example.c").toURI());


            input = CharStreams.fromPath(path);
        } catch (Exception e) {
            LOGGER.error("Error while parsing file.");
        }
        CLexer lexer = new CLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokens);

        ParserRuleContext tree = parser.compilationUnit();
    }
}
