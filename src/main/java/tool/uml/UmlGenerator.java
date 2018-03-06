package tool.uml;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ast.model.ClassNode;
import tool.ast.model.TreeNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.attribute.Records.turn;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

/**
 *
 */
public class UmlGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmlGenerator.class);
    private String filePath;

    /**
     * Creates graph
     * @param ast AST tree
     * @return Graph for UML diagram
     */
    public Graph createUml(List<TreeNode> ast) {
        LOGGER.info("Start create uml");
        List<Node> classNodes = new ArrayList<>();
        for(TreeNode rootNode : ast) {
            LOGGER.info("Create UML for the root node {}", rootNode.getFilePath());
            for(TreeNode node : rootNode.getChildren()) {
                if (node.getNodeType().equals("class")) {
                    ClassNode classNode = (ClassNode) node;
                    String className = classNode.getName();
                    List<Attributes> attributes = new ArrayList<>();
                    attributes.add(Shape.RECTANGLE);

                    StringBuilder methods = new StringBuilder();
                    for (TreeNode classChild : classNode.getChildren()) {
                        if (classChild.getNodeType().equals("method")) {
                            methods.append(classChild.getName()).append("\n");
                        }
                    }
                    Node umlClassNode = node(classNode.getName())
                            .with(Records.of(turn(rec(className), rec("method", methods.toString()))));
                    classNodes.add(umlClassNode);
                }
            }
        }

        Node [] nodeArray = new Node[classNodes.size()];
        Graph graph = graph("UML diagram").directed().with(classNodes.toArray(nodeArray));
        return graph;
    }

    public void exportUml(Graph graph, String filePath, Format format) {
        try {
            Graphviz.fromGraph(graph).width(900).render(format)
                    .toFile(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
