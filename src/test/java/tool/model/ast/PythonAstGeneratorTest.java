package tool.model.ast;

import org.junit.jupiter.api.Test;
import tool.model.AstGenerator;
import tool.formats.FormatFactory;
import tool.formats.xml.XMLFactory;
import tool.model.TreeNode;
import tool.utils.FileManager;

public class PythonAstGeneratorTest {

    @Test
    public void pythonAstGenerationTest() {
        AstGenerator astGenerator = new AstGenerator();
        TreeNode ast = astGenerator.generatePythonAst("examples/python/python_example2.py");

        FormatFactory formatFactory = new XMLFactory();
        String xmlAst = formatFactory.format(ast);
        System.out.println(xmlAst);

        FileManager.saveFile("output/python_example.xml", xmlAst);
    }
}
