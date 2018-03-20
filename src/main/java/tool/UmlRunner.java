package tool;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import tool.console.Settings;
import tool.console.Type;
import tool.formats.uml.UmlConverter;
import tool.model.AstGenerator;
import tool.model.TreeNode;
import tool.utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UmlRunner extends Runner {

    public static void main(String ... args) {
        enterUmlSettings();
        System.out.println("Start AST generation.");
        AstGenerator astGenerator = new AstGenerator();
        List<TreeNode> asts = new ArrayList<>();

        List<String> filePaths = FileManager.getAllFilePaths(Settings.inputDir);
        for(String filePath : filePaths) {
            if (Settings.type == Type.PYTHON) {
                asts.add(astGenerator.generatePythonAst(filePath));
            } else {
                asts.add(astGenerator.generateCAst(filePath));
            }
        }
        System.out.println("AST generation is finished.");

        System.out.println("Start UML generation.");
        UmlConverter umlConverter = new UmlConverter();
        Graph graph = umlConverter.createUmlFromFilesList(asts);
        umlConverter.exportUml(graph, Settings.outputDir  + File.separator + "uml", Format.PNG);
        System.out.println("UML generation is finished.");
        System.out.println("Finish program");
    }
}
