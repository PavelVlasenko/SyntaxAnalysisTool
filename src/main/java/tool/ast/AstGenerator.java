package tool.ast;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.*;
import tool.visitors.CAstVisitor;
import tool.visitors.PythonAstVisitor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AstGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstGenerator.class);

    public void generatePythonAst() {
        LOGGER.info("Generate Python AST.");
        CharStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Path path = Paths.get(classLoader.getResource("examples/python/python_example.py").toURI());
            input = CharStreams.fromPath(path);
        } catch (Exception e) {
            LOGGER.error("Error while parsing file.");
        }
        Python3Lexer lexer = new Python3Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        ParseTree tree = parser.file_input();

        PythonAstVisitor visitor = new PythonAstVisitor();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);
    }

    public void generateCAst() {
        LOGGER.info("Generate C AST.");
        CharStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Path path = Paths.get(classLoader.getResource("examples/c/c_example.c").toURI());
            input = CharStreams.fromPath(path);
        } catch (Exception e) {
            LOGGER.error("Error while parsing file.");
        }
        CLexer lexer = new CLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokens);

        ParseTree tree = parser.compilationUnit();

        CAstVisitor visitor = new CAstVisitor();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);
    }
}
