package tool.ast;

import org.junit.jupiter.api.Test;
import tool.ast.model.TreeNode;
import tool.formats.FormatFactory;
import tool.formats.xml.XMLFactory;

public class PythonAstGeneratorTest {

    @Test
    public void pythonAstGenerationTest() {
        AstGenerator astGenerator = new AstGenerator();
        TreeNode ast = astGenerator.generatePythonAst("examples/python/python_example2.py");

        FormatFactory formatFactory = new XMLFactory();
        String xmlAst = formatFactory.format(ast);
        System.out.println(xmlAst);
    }
}
