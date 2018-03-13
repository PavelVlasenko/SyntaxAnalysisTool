package tool.formats.uml;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import org.junit.jupiter.api.Test;
import tool.model.AstGenerator;
import tool.model.TreeNode;

import java.util.Collections;

public class UmlConverterTest {

    @Test
    public void PythonUmlGenerationTest() {
        AstGenerator astGenerator = new AstGenerator();
        TreeNode ast = astGenerator.generatePythonAst("examples\\python\\python_example2.py");

        UmlConverter umlConverter = new UmlConverter();
        Graph graph = umlConverter.createUml(Collections.singletonList(ast));
        umlConverter.exportUml(graph, "C:\\Users\\SBT-Vlasenko-PV\\Test\\python_uml_diagram.png", Format.PNG);
    }

    @Test
    public void CUmlGenerationTest() {
        AstGenerator astGenerator = new AstGenerator();
        TreeNode ast = astGenerator.generateCAst("examples\\c\\c_example.c");

        UmlConverter umlConverter = new UmlConverter();
        Graph graph = umlConverter.createUml(Collections.singletonList(ast));
        umlConverter.exportUml(graph, "C:\\Users\\SBT-Vlasenko-PV\\Test\\c_uml_diagram.png", Format.PNG);
    }
}
