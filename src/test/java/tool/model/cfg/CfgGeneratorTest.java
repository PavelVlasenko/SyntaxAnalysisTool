package tool.model.cfg;

import org.junit.jupiter.api.Test;
import tool.model.CfgGenerator;

public class CfgGeneratorTest {

    @Test
    public void pythonAstGenerationTest() {
        CfgGenerator cfgGenerator = new CfgGenerator();
        cfgGenerator.generateCfg("examples/python/python_example2.py");
    }
}
