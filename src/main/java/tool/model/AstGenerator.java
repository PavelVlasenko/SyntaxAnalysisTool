package tool.model;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.*;
import tool.model.ast.ClassNode;
import tool.model.ast.RootNode;
import tool.visitors.ast.CAstVisitor;
import tool.visitors.ast.PythonAstVisitor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AstGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstGenerator.class);

    public TreeNode generatePythonAst(String fileName) {
        System.out.println("Generate Python AST.");
        Python3Lexer lexer = new Python3Lexer(getStream(fileName));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        ParseTree tree = parser.file_input();

        TreeNode ast = new RootNode("AST");
        ast.setFilePath(fileName);
        PythonAstVisitor visitor = new PythonAstVisitor(ast);

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);
        return ast;
    }

    public TreeNode generateCAst(String fileName) {
        System.out.println("Generate C AST.");
        CLexer lexer = new CLexer(getStream(fileName));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokens);

        ParseTree tree = parser.compilationUnit();

        TreeNode ast = new RootNode("AST");
        ast.setFilePath(fileName);
        ClassNode classNode = new ClassNode(fileName);
        classNode.setFilePath(fileName);
        CAstVisitor visitor = new CAstVisitor(classNode);
        ast.addChild(classNode);

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);

        return ast;
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
