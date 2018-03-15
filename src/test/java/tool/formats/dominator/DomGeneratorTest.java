package tool.formats.dominator;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tool.formats.cfg.GraphBuilder;
import tool.formats.dom.DomGenerator;
import tool.model.CfgGenerator;
import tool.model.cfg.EntryNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Disabled
public class DomGeneratorTest {

    @Test
    public void CCfgGraphDomTest() {
        CfgGenerator cfgGenerator = new CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgs = cfgGenerator.generateCCfg("examples\\c\\c_example.c");


        DomGenerator domGenerator = new DomGenerator(cfgs.values().iterator().next().get(0));
        domGenerator.generateDom();
        GraphBuilder graphBuilder = new GraphBuilder();
        List<EntryNode> cfgList = new ArrayList<>();
        for(ArrayList<EntryNode> list : cfgs.values()) {
            cfgList.addAll(list);
        }

        Map<String, Graph> graphs = graphBuilder.createGraph(cfgList);
        graphBuilder.exportGraphs(graphs, "C:\\Users\\SBT-Vlasenko-PV\\Test\\", Format.PNG);
    }

    @Test
    public void CCfgGraphPostDomTest() {
        CfgGenerator cfgGenerator = new CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgs = cfgGenerator.generateCCfg("examples\\c\\c_example.c");


        DomGenerator domGenerator = new DomGenerator(cfgs.values().iterator().next().get(0));
        domGenerator.generatePostDom();
        GraphBuilder graphBuilder = new GraphBuilder();
        List<EntryNode> cfgList = new ArrayList<>();
        for(ArrayList<EntryNode> list : cfgs.values()) {
            cfgList.addAll(list);
        }

        Map<String, Graph> graphs = graphBuilder.createGraph(cfgList);
        graphBuilder.exportGraphs(graphs, "C:\\Users\\SBT-Vlasenko-PV\\Test\\", Format.PNG);
    }
}
