package tool.uml;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import org.junit.jupiter.api.Test;
import tool.ast.AstGenerator;
import tool.ast.model.TreeNode;

import java.util.Collections;

public class UmlGeneratorTest {

    @Test
    public void UmlGenerationTest() {
        AstGenerator astGenerator = new AstGenerator();
        TreeNode ast = astGenerator.generatePythonAst("examples/python/python_example2.py");

        UmlGenerator umlGenerator = new UmlGenerator();
        Graph graph = umlGenerator.createUml(Collections.singletonList(ast));
        umlGenerator.exportUml(graph, "uml_diagram.png", Format.PNG);
    }
}
