package tool;

import tool.console.Settings;
import tool.console.Type;
import tool.formats.csv.CsvConverter;
import tool.formats.xml.FormatFactory;
import tool.formats.xml.XMLFactory;
import tool.model.AstGenerator;
import tool.model.TreeNode;
import tool.utils.FileManager;

import java.io.File;


public class AstRunner extends Runner {

    public static void main(String ... args) {
        enterSettings();
        System.out.println("Start AST generator.");
        AstGenerator astGenerator = new AstGenerator();
        TreeNode ast;
        if(Settings.type == Type.PYTHON) {
            ast = astGenerator.generatePythonAst(Settings.inputFilePath);
        }
        else {
            ast = astGenerator.generateCAst(Settings.inputFilePath);
        }
        System.out.println("AST tree is generated.");

        FormatFactory formatFactory = new XMLFactory();
        String xmlAst = formatFactory.format(ast);
        System.out.println(xmlAst);

        System.out.println("Save AST in XML format.");
        FileManager.saveFile(Settings.outputDir + File.separator  + "ast.xml", xmlAst);

        System.out.println("Generate csv metrics");
        CsvConverter csvConverter = new CsvConverter();
        String metrics = csvConverter.getMetricsInCsv(ast.getChildren());
        FileManager.saveFile(Settings.outputDir + File.separator + "metrics.csv", metrics);
        System.out.println("Finish program");
    }

}
