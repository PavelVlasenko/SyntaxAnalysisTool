package tool.formats.cfg;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.model.GraphNode;
import tool.model.cfg.EntryNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;


public class CfgConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CfgConverter.class);
    HashMap<Integer, Node> nodes = new HashMap<>();
    List<Node> direcedNodes = new ArrayList<>();


    public List<Graph> createGraph(List<EntryNode> cfgs) {
        LOGGER.info("Start create uml");
        for(EntryNode cfgNode : cfgs) {
            LOGGER.info("Process  cfgNode {}", cfgNode.getFilePath());
            processCFG(cfgNode);
            createGraph(cfgNode);
        }
        Node[] directedArray = new Node[direcedNodes.size()];
        Graph g = graph("cfg").directed().with(direcedNodes.toArray(directedArray));
        return Collections.singletonList(g);
    }

    private void createGraph(GraphNode node) {
        for(GraphNode childNode : node.getSuccessors()) {
            LOGGER.info("Process node id={}, name={}", childNode.getId(), childNode.getName());
            Node from = nodes.get(node.getId());
            Node to = nodes.get(childNode.getId());
            direcedNodes.add(from.link(to));
            createGraph(childNode);
        }
    }

    private void processCFG(EntryNode entryNode) {
        nodes.put(entryNode.getId(), node(entryNode.getName()));
        processNode(entryNode);
    }

    private void processNode(GraphNode cfgNode) {
        for(GraphNode graphNode : cfgNode.getSuccessors()) {
            nodes.put(graphNode.getId(), node(graphNode.getName()));
            processNode(graphNode);
        }
    }

    public void exportGraph(Graph graph, String filePath, Format format) {
        try {
            Graphviz.fromGraph(graph).width(900).render(format)
                    .toFile(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
