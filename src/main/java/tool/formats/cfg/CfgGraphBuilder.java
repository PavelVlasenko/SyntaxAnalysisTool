package tool.formats.cfg;

import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.model.GraphNode;
import tool.model.cfg.EntryNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class CfgGraphBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(CfgGraphBuilder.class);
    private HashMap<Integer, Node> nodes = new HashMap<>();
    private List<Node> direcedNodes = new ArrayList<>();

    public Graph createGraph(EntryNode entryNode) {
        processCFG(entryNode);
        processGraph(entryNode);

        Node[] directedArray = new Node[direcedNodes.size()];
        Graph graph = graph("CfgGraph").directed().with(direcedNodes.toArray(directedArray));
        return graph;
    }

    private void processGraph(GraphNode node) {
        for(GraphNode childNode : node.getSuccessors()) {
            LOGGER.info("Process node id={}, name={}", childNode.getId(), childNode.getName());
            Node from = nodes.get(node.getId());
            Node to = nodes.get(childNode.getId());
            direcedNodes.add(from.link(to));
            processGraph(childNode);
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
}
