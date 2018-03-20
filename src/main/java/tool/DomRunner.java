package tool;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import tool.console.Settings;
import tool.formats.cfg.GraphBuilder;
import tool.formats.dom.DomGenerator;
import tool.model.CfgGenerator;
import tool.model.cfg.EntryNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Runner for generating DOM
 */
public class DomRunner extends Runner {

    public static void main(String ... args) {
        enterSettings();
        System.out.println("Start the program");
        System.out.println("Generate CFG");

        //Generate CFG
        CfgGenerator cfgGenerator = new CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgsMap = cfgGenerator.generateCCfg(Settings.inputFilePath);
        System.out.println("CFG is generated");

        List<EntryNode> cfgs = new ArrayList<>();
        for(ArrayList<EntryNode> list : cfgsMap.values()) {
            cfgs.addAll(list);
        }

        System.out.println("Start generate graphs");
        List<EntryNode> resultGraphs = new ArrayList<>();
        cfgs.forEach((cfg) -> {
            DomGenerator domGenerator = new DomGenerator(cfg);
            EntryNode entryNode = domGenerator.generateDom();
            resultGraphs.add(entryNode);
        });

        System.out.println("Export graphs to files");
        GraphBuilder graphBuilder = new GraphBuilder();
        Map<String, Graph> graphs = graphBuilder.createGraph(resultGraphs);
        graphBuilder.exportGraphs(graphs, Settings.outputDir, Format.PNG);
        System.out.println("Finish program");
    }
}
