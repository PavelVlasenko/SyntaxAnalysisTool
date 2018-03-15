package tool;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import tool.console.Settings;
import tool.formats.cfg.GraphBuilder;
import tool.model.CfgGenerator;
import tool.model.cfg.EntryNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AstRunner extends Runner {

    public static void main(String ... args) {
        enterSettings();
        System.out.println("Start AST generator.");
        CfgGenerator cfgGenerator = new CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgs = cfgGenerator.generateCCfg(Settings.inputFilePath);

        GraphBuilder graphBuilder = new GraphBuilder();
        List<EntryNode> cfgList = new ArrayList<>();
        for(ArrayList<EntryNode> list : cfgs.values()) {
            cfgList.addAll(list);
        }

        Map<String, Graph> graphs = graphBuilder.createGraph(cfgList);
        graphBuilder.exportGraphs(graphs, "C:\\Users\\SBT-Vlasenko-PV\\Test\\", Format.PNG);

    }

}
