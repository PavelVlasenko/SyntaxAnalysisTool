package tool.formats.uml;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tool.UmlRunner;
import tool.console.Settings;
import tool.console.Type;
import tool.model.AstGenerator;
import tool.model.TreeNode;

import java.util.Collections;

//@Disabled
public class UmlConverterTest {

    @Test
    public void PythonUmlGenerationTest() {
        Settings.test = true;
        Settings.inputDir = "C:\\Users\\SBT-Vlasenko-PV\\Test\\input";
        Settings.outputDir = "C:\\Users\\SBT-Vlasenko-PV\\Test\\output";
        Settings.type = Type.PYTHON;

        UmlRunner.main();
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
