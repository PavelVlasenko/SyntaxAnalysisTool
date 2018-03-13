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
    private List<Node> directedNodes = new ArrayList<>();
    private List<GraphNode> visited = new ArrayList<>();

    public Graph createGraph(EntryNode entryNode) {
        createNodes(entryNode);

        visited = new ArrayList<>();
        processGraph(entryNode);

        Node[] directedArray = new Node[directedNodes.size()];
        Graph graph = graph("CfgGraph").directed().with(directedNodes.toArray(directedArray));
        return graph;
    }

    private void processGraph(GraphNode node) {
        if(visited.contains(node)) {
            LOGGER.info("Skip node, cause it's already visited id={}, name={}", node.getId(), node.getName());
            return;
        }
        LOGGER.info("Process node id={}, name={}", node.getId(), node.getName());

        List<Node> childNodes = new ArrayList<>();
        for(GraphNode childNode : node.getSuccessors()) {
            Node to = nodes.get(childNode.getId());
            childNodes.add(to);
        }
        Node from = nodes.get(node.getId());
        Node[] nodesArray = new Node[childNodes.size()];
        directedNodes.add(from.link(childNodes.toArray(nodesArray)));
        visited.add(node);
        for(GraphNode childNode : node.getSuccessors()) {
            processGraph(childNode);
        }
    }

    private void createNodes(EntryNode entryNode) {
        nodes.put(entryNode.getId(), node(entryNode.getName()));
        createNode(entryNode);
        visited.add(entryNode);
    }

    private void createNode(GraphNode cfgNode) {
        for(GraphNode graphNode : cfgNode.getSuccessors()) {
            if(!visited.contains(graphNode)) {
                nodes.put(graphNode.getId(), node(graphNode.getName()));
                visited.add(graphNode);
                createNode(graphNode);
            }
        }
    }
}
