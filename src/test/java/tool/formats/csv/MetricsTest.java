package tool.formats.csv;

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

        CsvConverter csvConverter = new CsvConverter();
        String metrics = csvConverter.getMetricsInCsv(Collections.singletonList(ast));

        FileManager.saveFile("output/csv.csv", metrics);
    }

}
