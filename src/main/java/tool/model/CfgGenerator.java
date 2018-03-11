package tool.model;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.Python3Lexer;
import tool.antlr4.Python3Parser;
import tool.model.ast.RootNode;
import tool.model.cfg.EntryNode;
import tool.visitors.ast.PythonAstVisitor;
import tool.visitors.cfg.CfgVisitor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class CfgGenerator {
    public static final Logger LOGGER = LoggerFactory.getLogger(CfgGenerator.class);

    public HashMap<String, ArrayList<EntryNode>> generateCfg(String fileName) {
        LOGGER.info("Generate Python AST.");
        Python3Lexer lexer = new Python3Lexer(getStream(fileName));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        ParseTree tree = parser.file_input();

        CfgVisitor visitor = new CfgVisitor();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);

        return visitor.getCFGs();
    }

    private CharStream getStream(String fileName) {
        CharStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Path path = Paths.get(classLoader.getResource(fileName).toURI());
            input = CharStreams.fromPath(path);
        } catch (Exception e) {
            LOGGER.error("Error while parsing file.");
        }
        return input;
    }

}
