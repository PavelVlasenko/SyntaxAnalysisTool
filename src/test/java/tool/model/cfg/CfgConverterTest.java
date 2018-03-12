package tool.model.cfg;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import org.junit.jupiter.api.Test;
import tool.formats.cfg.CfgConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CfgConverterTest {

    @Test
    public void pythonAstGenerationTest() {
        tool.model.CfgGenerator cfgGenerator = new tool.model.CfgGenerator();
        cfgGenerator.generateCfg("examples/python/python_cfg_example.py");
    }

    @Test
    public void pythonCfgGraphTest() {
        tool.model.CfgGenerator cfgGenerator = new tool.model.CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgs = cfgGenerator.generateCfg("examples/python/python_cfg_example.py");

        CfgConverter graphBuilder = new CfgConverter();
        List<EntryNode> cfgList = new ArrayList<>();
        for(ArrayList<EntryNode> list : cfgs.values()) {
            cfgList.addAll(list);
        }

        Map<String, Graph> graphs = graphBuilder.createGraph(cfgList);
        graphBuilder.exportGraphs(graphs, "C:\\Users\\SBT-Vlasenko-PV\\Test\\", Format.PNG);
    }
}