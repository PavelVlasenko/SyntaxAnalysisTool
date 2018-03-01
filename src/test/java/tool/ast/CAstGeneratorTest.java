package tool.ast;

import org.junit.jupiter.api.Test;
import tool.ast.model.TreeNode;
import tool.formats.FormatFactory;
import tool.formats.xml.XMLFactory;
import tool.utils.FileManager;

public class CAstGeneratorTest {

    @Test
    public void cAstGenerationTest() {
        AstGenerator astGenerator = new AstGenerator();
        TreeNode ast = astGenerator.generateCAst("examples/c/c_example.c");

        FormatFactory formatFactory = new XMLFactory();
        String xmlAst = formatFactory.format(ast);
        System.out.println(xmlAst);

        FileManager.saveFile("output/c_example.xml", xmlAst);
    }
}
