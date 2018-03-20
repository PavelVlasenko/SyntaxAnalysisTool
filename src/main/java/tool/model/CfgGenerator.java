package tool.model;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import tool.antlr4.CLexer;
import tool.antlr4.CParser;
import tool.antlr4.Python3Lexer;
import tool.antlr4.Python3Parser;
import tool.model.cfg.EntryNode;
import tool.utils.CharStreamUtils;
import tool.visitors.cfg.CCfgVisitor;
import tool.visitors.cfg.PythonCfgVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generates CFG from source code file.
 */
public class CfgGenerator {

    /**
     * Generates Python CFG
     * @param fileName path to the file
     * @return Map with key - method name, and value - CFG tree
     */
    public HashMap<String, ArrayList<EntryNode>> generatePythonCfg(String fileName) {
        System.out.println("Generate Python CFG.");

        //Create Lexer
        Python3Lexer lexer = new Python3Lexer(CharStreamUtils.getStream(fileName));

        //Create tokens parser
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        //Generate AST
        ParseTree tree = parser.file_input();

        //Create visitor
        PythonCfgVisitor visitor = new PythonCfgVisitor(fileName.substring(fileName.lastIndexOf(File.separator) + 1));

        //Walk through tree and generate Abstract AST
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);

        return visitor.getCFGs();
    }

    /**
     * Generates C CFG
     * @param fileName path to the file
     * @return Map with key - method name, and value - CFG tree
     */
    public HashMap<String, ArrayList<EntryNode>> generateCCfg(String fileName) {
        System.out.println("Generate C CFG.");

        //Create Lexer
        CLexer lexer = new CLexer(CharStreamUtils.getStream(fileName));

        //Create token parser
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokens);

        //Generate  AST
        ParseTree tree = parser.compilationUnit();

        //Create visitors
        CCfgVisitor visitor = new CCfgVisitor(fileName);

        //Walk through tree and generate Abstract AST
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);

        return visitor.getCFGs();
    }
}
