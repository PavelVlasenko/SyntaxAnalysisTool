package tool.model;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import tool.antlr4.*;
import tool.model.ast.ClassNode;
import tool.model.ast.RootNode;
import tool.utils.CharStreamUtils;
import tool.visitors.ast.CAstVisitor;
import tool.visitors.ast.PythonAstVisitor;

/**
 * Generates AST tree from source code file.
 */
public class AstGenerator {

    /**
     * Generates Python AST
     * @param fileName path to the file.
     * @return AST tree
     */
    public TreeNode generatePythonAst(String fileName) {
        System.out.println("Generate Python AST.");

        //Create Lexer
        Python3Lexer lexer = new Python3Lexer(CharStreamUtils.getStream(fileName));

        //Create token parser
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        //Generate AST tree
        ParseTree tree = parser.file_input();

        //Create ROOT abstract tree.
        TreeNode ast = new RootNode("AST");
        ast.setFilePath(fileName);

        //Create Python visitor
        PythonAstVisitor visitor = new PythonAstVisitor(ast);

        //Walk through tree
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);
        return ast;
    }

    /**
     * Generates C AST. All logic the same as in Python language.
     * @param fileName path to the file.
     * @return AST tree
     */
    public TreeNode generateCAst(String fileName) {
        System.out.println("Generate C AST.");
        CLexer lexer = new CLexer(CharStreamUtils.getStream(fileName));
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
}
