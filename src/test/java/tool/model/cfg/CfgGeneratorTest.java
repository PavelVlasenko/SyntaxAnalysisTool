package tool.model.cfg;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tool.formats.cfg.GraphBuilder;
import tool.model.CfgGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Disabled
public class CfgGeneratorTest {

    @Test
    public void pythonAstGenerationTest() {
        tool.model.CfgGenerator cfgGenerator = new tool.model.CfgGenerator();
        cfgGenerator.generatePythonCfg("examples\\python\\python_cfg_example.py");
    }

    @Test
    public void pythonCfgGraphTest() {
        CfgGenerator cfgGenerator = new CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgs = cfgGenerator.generatePythonCfg("examples\\python\\python_cfg_example.py");

        GraphBuilder graphBuilder = new GraphBuilder();
        List<EntryNode> cfgList = new ArrayList<>();
        for(ArrayList<EntryNode> list : cfgs.values()) {
            cfgList.addAll(list);
        }

        Map<String, Graph> graphs = graphBuilder.createGraph(cfgList);
        graphBuilder.exportGraphs(graphs, "C:\\Users\\SBT-Vlasenko-PV\\Test\\", Format.PNG);
    }

    @Test
    public void CCfgGraphTest() {
        CfgGenerator cfgGenerator = new CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgs = cfgGenerator.generateCCfg("examples\\c\\c_example.c");

        GraphBuilder graphBuilder = new GraphBuilder();
        List<EntryNode> cfgList = new ArrayList<>();
        for(ArrayList<EntryNode> list : cfgs.values()) {
            cfgList.addAll(list);
        }

        Map<String, Graph> graphs = graphBuilder.createGraph(cfgList);
        graphBuilder.exportGraphs(graphs, "C:\\Users\\SBT-Vlasenko-PV\\Test\\", Format.PNG);
    }
}
