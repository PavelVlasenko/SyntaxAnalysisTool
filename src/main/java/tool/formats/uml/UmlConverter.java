package tool.formats.uml;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import tool.model.ast.ClassNode;
import tool.model.TreeNode;
import tool.utils.FormatResolver;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.attribute.Records.turn;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

/**
 *  Generates UML
 */
public class UmlConverter {

    private List<ClassNode> classes = new ArrayList<>();
    private Map<Node, Set<Node>> linkedNodes = new LinkedHashMap<>();

    public UmlConverter() {
    }

    public Graph createUmlFromFilesList(List<TreeNode> source) {
        filterClasses(source);

        List<Node> classNodes = new ArrayList<>();
        Map<String, Node> nodesByName = new HashMap<>();
        for(ClassNode node : classes) {
            System.out.println("Create UML for the node " + node.getFilePath());
            String className = node.getName();

            StringBuilder methods = new StringBuilder();
            for (TreeNode classChild : node.getChildren()) {
                if (classChild.getNodeType().equals("method")) {
                    methods.append(classChild.getName()).append("\n");
                }
            }
            Node umlClassNode = node(node.getName())
                    .with(Records.of(turn(rec(className), rec("method", methods.toString()))));
            classNodes.add(umlClassNode);
            nodesByName.put(className, umlClassNode);
        }

        for(ClassNode classNode : classes) {
            String extendedClass = classNode.getExtension();
            if(extendedClass != null) {
                Set<Node> childs = linkedNodes.get(nodesByName.get(extendedClass));
                if(childs == null) {
                    childs = new HashSet<>();
                }
                childs.add(nodesByName.get(classNode.getName()));
                linkedNodes.put(nodesByName.get(extendedClass), childs);
            }
        }

        List<Node> graphNodes = new ArrayList<>();
        Set<Node> usedNodes = new HashSet<>();
        for(Map.Entry<Node, Set<Node>> entry : linkedNodes.entrySet()) {
            Node[] linkedNodes = new Node[entry.getValue().size()];
            graphNodes.add(entry.getKey().link(entry.getValue().toArray(linkedNodes)));
            usedNodes.add(entry.getKey());
            usedNodes.addAll(entry.getValue());
        }
        for(Node singleNode : classNodes) {
            if(!usedNodes.contains(singleNode)) {
                graphNodes.add(singleNode);
            }
        }

        Node [] nodesArray = new Node [graphNodes.size()];
        Graph graph = graph("CfgGraph").directed().with(graphNodes.toArray(nodesArray));
        return graph;
    }

    private void filterClasses(List<TreeNode> source) {
        if(source.isEmpty()) return;
        for(TreeNode node : source) {
            if(node instanceof ClassNode) {
                classes.add((ClassNode)node);
            }
            else {
                filterClasses(node.getChildren());
            }
        }
    }


    /**
     * Creates graph
     * @param ast AST tree
     * @return Graph for UML diagram
     */
    public Graph createUml(List<TreeNode> ast) {
        System.out.println("Start create uml");
        List<Node> classNodes = new ArrayList<>();
        for(TreeNode rootNode : ast) {
            System.out.println("Create UML for the root node " + rootNode.getFilePath());
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
                    .toFile(new File(filePath  + "_" + FormatResolver.resolve(format)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
