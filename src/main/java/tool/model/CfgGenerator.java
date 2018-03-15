package tool.model;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.CLexer;
import tool.antlr4.CParser;
import tool.antlr4.Python3Lexer;
import tool.antlr4.Python3Parser;
import tool.model.cfg.EntryNode;
import tool.utils.CharStreamUtils;
import tool.visitors.cfg.CCfgVisitor;
import tool.visitors.cfg.PythonCfgVisitor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class CfgGenerator {
    public static final Logger LOGGER = LoggerFactory.getLogger(CfgGenerator.class);

    public HashMap<String, ArrayList<EntryNode>> generatePythonCfg(String fileName) {
        System.out.println("Generate Python CFG.");
        Python3Lexer lexer = new Python3Lexer(CharStreamUtils.getStream(fileName));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);

        ParseTree tree = parser.file_input();

        PythonCfgVisitor visitor = new PythonCfgVisitor(fileName.substring(fileName.lastIndexOf(File.separator) + 1));

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);

        return visitor.getCFGs();
    }

    public HashMap<String, ArrayList<EntryNode>> generateCCfg(String fileName) {
        System.out.println("Generate C CFG.");
        CLexer lexer = new CLexer(CharStreamUtils.getStream(fileName));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokens);

        ParseTree tree = parser.compilationUnit();

        CCfgVisitor visitor = new CCfgVisitor(fileName);

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(visitor, tree);

        return visitor.getCFGs();
    }
}
