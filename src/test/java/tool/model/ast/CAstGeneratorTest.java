package tool.model.ast;

import org.junit.jupiter.api.Test;
import tool.model.AstGenerator;
import tool.formats.xml.FormatFactory;
import tool.formats.xml.XMLFactory;
import tool.model.TreeNode;
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
