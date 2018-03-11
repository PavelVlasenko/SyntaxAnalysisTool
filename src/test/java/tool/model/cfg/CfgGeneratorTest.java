package tool.model.cfg;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import org.junit.jupiter.api.Test;
import tool.cfg_graph.CfgGraphBuilder;
import tool.model.CfgGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CfgGeneratorTest {

    @Test
    public void pythonAstGenerationTest() {
        CfgGenerator cfgGenerator = new CfgGenerator();
        cfgGenerator.generateCfg("examples/python/python_cfg_example.py");
    }

    @Test
    public void pythonCfgGraphTest() {
        CfgGenerator cfgGenerator = new CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgs = cfgGenerator.generateCfg("examples/python/python_cfg_example.py");

        CfgGraphBuilder graphBuilder = new CfgGraphBuilder();
        List<EntryNode> cfgList = new ArrayList<>();
        for(ArrayList<EntryNode> list : cfgs.values()) {
            cfgList.addAll(list);
        }

        List<Graph> graphs = graphBuilder.createGraph(cfgList);
        graphBuilder.exportGraph(graphs.get(0), "C:\\Users\\SBT-Vlasenko-PV\\Test\\uml_diagram.png", Format.PNG);
    }
}
