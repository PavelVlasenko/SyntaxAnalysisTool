package tool.formats.cfg;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import tool.model.cfg.EntryNode;
import tool.utils.FormatResolver;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Creates CFG graphs in GraphViz format from CFG tree.
 */
public class GraphBuilder {

    public Map<String, Graph> createGraph(List<EntryNode> cfgs) {
        System.out.println("Start convert cfg");
        Map<String, Graph> result = new HashMap<>();

        //For every CFG tree we process it and add to the result map
        for(EntryNode entryNode : cfgs) {
            System.out.println("Process  cfgNode" + entryNode.getName());

            //Create builder for every CFG tree, and build graph
            CfgToGraphConverter builder = new CfgToGraphConverter();
            Graph graph = builder.createGraph(entryNode);
            String entryNodeName = entryNode.getName();

            //Read method name
            String filePath = entryNode.getFilePath();
            String cleanFileName;
            if(filePath.contains(File.separator)) {
               cleanFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
            }
            else {
                cleanFileName = filePath;
            }
            result.put(cleanFileName + "_" +
            entryNodeName.substring(entryNodeName.indexOf("\n") + 1), graph);
        }
        return result;
    }

    /**
     * Export graphs to the files.
     * @param graphs list of graphs.
     * @param pathPrefix prefix fo files(output directory).
     * @param format file format(Can be any GraphViz format such as  PNG, DOT, etc)
     */
    public void exportGraphs( Map<String, Graph> graphs, String pathPrefix, Format format) {
        for(Map.Entry<String, Graph> graphEntry : graphs.entrySet()) {
            exportGraph(graphEntry.getValue(), pathPrefix + File.separator + graphEntry.getKey(), format);
        }
    }

    public void exportGraph(Graph graph, String filePath, Format format) {
        System.out.println("Save CFG graph to " + filePath);
        try {
            Graphviz.fromGraph(graph).width(900).render(format)
                    .toFile(new File(filePath  + "_" + FormatResolver.resolve(format)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
