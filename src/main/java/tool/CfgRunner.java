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

public class CfgRunner extends Runner{

    public static void main(String ... args) {
        enterSettings();
        System.out.println("Start AST generator.");
        CfgGenerator cfgGenerator = new CfgGenerator();
        HashMap<String, ArrayList<EntryNode>> cfgsMap = cfgGenerator.generateCCfg(Settings.inputFilePath);

        List<EntryNode> cfgs = new ArrayList<>();
        cfgsMap.values().stream().forEach((cfgList) -> cfgs.addAll(cfgList));
        System.out.println("Generated " + cfgs.size() + " cfgs");

        System.out.println("Export graphs to the outputDir: " + Settings.outputDir);
        GraphBuilder graphBuilder = new GraphBuilder();
        Map<String, Graph> graphs = graphBuilder.createGraph(cfgs);
        graphBuilder.exportGraphs(graphs, Settings.outputDir, Format.PNG);
        System.out.println("Finish program");
    }
}
