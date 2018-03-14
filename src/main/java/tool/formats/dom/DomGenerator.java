package tool.formats.dom;

import guru.nidi.graphviz.model.Graph;
import tool.model.GraphNode;
import tool.model.cfg.EntryNode;
import tool.model.cfg.ExitNode;
import java.util.*;


public class DomGenerator {

    List<LinkedList<GraphNode>> ways = new ArrayList<>();

    private LinkedHashMap<Integer, GraphNode> nodesByIndex = new LinkedHashMap<>();
    private LinkedHashMap<GraphNode, Integer> indexByNodes = new LinkedHashMap<>();
    private Set<GraphNode> processedNodes = new HashSet<>();

    private Set<GraphNode> waitingNodes = new HashSet<>();

    private List<GraphNode> visited = new ArrayList<>();

    private List<GraphNode> visitedDom = new ArrayList<>();
    private EntryNode entryNode;
    private Boolean [][] matrix;
    private int matrixSize = 0;

    private int index = 0;

    private ExitNode exitNode;

    public DomGenerator(EntryNode entryNode) {
        this.entryNode = entryNode;
    }

    public void generateDom() {
        getWays(entryNode, new LinkedList<>(), new HashSet<>());
        createNodes(entryNode);
        createMatrix();
        fillMatrix(entryNode, new ArrayList<>());
        printMatrix();
        clearMatrix();
        System.out.println("_______________________________");
        printMatrix();
        exportGraph();
    }

    public void generatePostDom() {
        generateDom();
    }

    private void createMatrix() {
        matrix = new Boolean [nodesByIndex.size()][nodesByIndex.size()];
        matrixSize = nodesByIndex.size();
    }

    private void fillMatrix(GraphNode from, List<GraphNode> visitedHigh) {
        visitedHigh.add(from);
        for(GraphNode to : from.getSuccessors()) {
            calculateDomFromChildNodes(from, to, new HashSet<>());
        }
        for(GraphNode fromChild : from.getSuccessors()) {
            if(!visitedHigh.contains(fromChild)) {
                fillMatrix(fromChild, visitedHigh);
            }
        }
    }

    private void calculateDomFromChildNodes(GraphNode from, GraphNode to, Set<GraphNode> visited) {
        matrix[indexByNodes.get(from)][indexByNodes.get(to)] = isImmediateDom(from, to);
        visited.add(from);
        visited.add(to);
        for(GraphNode toChild : to.getSuccessors()) {
            if(!visited.contains(from) || !visited.contains(toChild)) {
                calculateDomFromChildNodes(from, toChild, visited);
            }
        }
    }

    private boolean isImmediateDom(GraphNode from, GraphNode to) {
        for(LinkedList<GraphNode> way : ways) {
            boolean containsTo = false;
            boolean containsFrom = false;
            for(GraphNode node : way) {
                if(node.equals(to)) {
                    containsTo = true;
                }
                if(node.equals(from)) {
                    containsFrom = true;
                }
            }
            if(containsTo == true && containsFrom != true) {
                return false;
            }
        }
        return true;
    }



    private void getWays(GraphNode node, LinkedList<GraphNode> way, Set<GraphNode> visitedNodes) {
        way.add(node);
        if(node instanceof ExitNode || visitedNodes.contains(node)) {
            System.out.println("Finish way");
            ways.add(way);
        }
        else {
            visitedNodes.add(node);
            for(GraphNode childNode : node.getSuccessors()) {
                LinkedList<GraphNode> childWay = new LinkedList<>(way);
                HashSet<GraphNode> childVisitedNodes = new HashSet<>(visitedNodes);
                getWays(childNode, childWay, childVisitedNodes);
            }
        }
    }

    private boolean isCyclicWay(LinkedList<GraphNode> way) {
        return way.getLast() instanceof ExitNode;
    }

    private void createNodes(GraphNode node) {
        if(!visited.contains(node)) {
            int index = getIndex();
            nodesByIndex.put(index, node);
            indexByNodes.put(node, index);
            visited.add(node);
        }
        else {
            return;
        }
        for(GraphNode child : node.getSuccessors()) {
            createNodes(child);
        }
    }


    private void addNode(GraphNode node) {
        nodesByIndex.put(getIndex(), node);
        indexByNodes.put(node, getIndex());
        processedNodes.add(node);
        visited.add(node);
    }

    private int getIndex() {
        index++;
        return (index -1);
    }

    private boolean notProcessedParents(GraphNode node) {
        for(GraphNode parants : node.getPredecessors()) {
            if(!processedNodes.contains(parants)) {
                return true;
            }
        }
        return false;
    }

    private void processWaitingNodes() {
        Set<GraphNode> processed = new HashSet<>();
        for(GraphNode node : waitingNodes) {
            if(notProcessedParents(node)) {
                continue;
            }
            else {
                processed.add(node);
                addNode(node);
            }
        }
        for(GraphNode node : processed) {
            waitingNodes.remove(node);
        }
    }

    private void printMatrix() {
        for(Boolean [] mat : matrix) {
            for (Boolean val : mat) {
                if(val== null) {
                    System.out.print(0);
                }
                else if(val.booleanValue()) {
                    System.out.print(1);
                }
                else {
                    System.out.print(0);
                }
            }
            System.out.println();
        }
    }

    private void clearMatrix() {
        for(int i = 0 ; i < matrixSize; i++) {
            for(int j = 0; j < i; j++) {
                matrix[i][j] = false;
            }
        }
    }

    public void exportGraph() {
        clearGraph();
        for(int i = 0; i < matrixSize; i++) {
            for(int j = i; j < matrixSize; j++) {
                int count = 0;
                int i2 = 0;
                int j2 = 0;
                for(int k = i; k < matrixSize; k++) {
                    if(matrix[k][j] != null && matrix[k][j].equals(true)) {
                        count++;
                        i2 = k;
                        j2 = j;
                    }
                }
                if(count == 1) {
                    nodesByIndex.get(i2).addSuccessor(nodesByIndex.get(j2));
                }
            }

            for(int j3 = 0; j3 < matrixSize; j3++) {
                matrix[i][j3] = false;
            }
        }
    }

    private void clearGraph() {
        for(GraphNode node : nodesByIndex.values()) {
            node.resetSuccessors();
        }
    }

//    public void exportGraph(String filePath, Format format) {
//        CfgGraphBuilder builder = new CfgGraphBuilder();
//        Graph graph = builder.createGraph(entryNode);
//        String entryNodeName = entryNode.getName();
//
//        try {
//            Graphviz.fromGraph(graph).width(900).render(format)
//                    .toFile(new File(filePath  + "_" + FormatResolver.resolve(format)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
