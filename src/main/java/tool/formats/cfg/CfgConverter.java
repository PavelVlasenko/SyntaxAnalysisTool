package tool.formats.cfg;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.model.cfg.EntryNode;
import tool.utils.FormatResolver;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CfgConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CfgConverter.class);

    public Map<String, Graph> createGraph(List<EntryNode> cfgs) {
        LOGGER.info("Start convert cfg");
        Map<String, Graph> result = new HashMap<>();
        for(EntryNode entryNode : cfgs) {
            LOGGER.info("Process  cfgNode {}", entryNode.getName());
            CfgGraphBuilder builder = new CfgGraphBuilder();
            Graph graph = builder.createGraph(entryNode);
            String entryNodeName = entryNode.getName();
            result.put(entryNode.getFilePath() + "_" +
            entryNodeName.substring(entryNodeName.indexOf("\n") + 1), graph);
        }
        return result;
    }

    public void exportGraphs( Map<String, Graph> graphs, String pathPrefix, Format format) {
        for(Map.Entry<String, Graph> graphEntry : graphs.entrySet()) {
            exportGraph(graphEntry.getValue(), pathPrefix + graphEntry.getKey(), format);
        }
    }

    public void exportGraph(Graph graph, String filePath, Format format) {
        LOGGER.info("Save CFG graph to {}", filePath);
        try {
            Graphviz.fromGraph(graph).width(900).render(format)
                    .toFile(new File(filePath  + "_" + FormatResolver.resolve(format)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
