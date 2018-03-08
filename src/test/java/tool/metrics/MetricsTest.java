package tool.metrics;

import org.junit.jupiter.api.Test;
import tool.model.AstGenerator;
import tool.model.TreeNode;
import tool.utils.FileManager;

import java.util.Collections;

public class MetricsTest {

    @Test
    public void metricsTest() {
        AstGenerator astGenerator = new AstGenerator();
        TreeNode ast = astGenerator.generatePythonAst("examples/python/python_example2.py");

        MetricsConverter metricsConverter = new MetricsConverter();
        String metrics = metricsConverter.getMetricsInCsv(Collections.singletonList(ast));

        FileManager.saveFile("output/metrics.csv", metrics);
    }

}
