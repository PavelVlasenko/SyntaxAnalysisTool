package tool.model;

import tool.model.cfg.ExitNode;

import java.util.ArrayList;

public class GraphNode extends MyNode {
    protected ArrayList<GraphNode> predecessors;
    protected ArrayList<GraphNode> successors;
    protected boolean noOtherSuccessor = false;
    protected ArrayList<GraphNode> visited;

    public GraphNode(String name) {
        super(name);
        this.predecessors = new ArrayList<>();
        this.successors = new ArrayList<>();
    }

    public GraphNode(MyNode node) {
        super(node);
        this.predecessors = new ArrayList<>();
        this.successors = new ArrayList<>();
    }


    public ArrayList<GraphNode> getPredecessors() {
        return predecessors;
    }

    public ArrayList<GraphNode> getSuccessors() {
        return successors;
    }

    public void addSuccessor(GraphNode node) {
        if(noOtherSuccessor)
            return;
        if(this.getSuccessors().contains(node)) {
            return;
        }
        node.predecessors.add(this);
        successors.add(node);
    }

    public void addSuccessor(int index, GraphNode node) {
        if(noOtherSuccessor)
            return;
        node.predecessors.add(this);
        successors.add(index, node);
    }

    public void removeSuccessor(int index) {
        this.successors.remove(index);
    }

    public ArrayList<GraphNode> getLeaves() {
        ArrayList<GraphNode> res = new ArrayList<>();
        this.visited = new ArrayList<>();
        this.visited.add(this);

        this.leaves(this, res);

        return res;
    }

    private void leaves(GraphNode graphNode, ArrayList<GraphNode> res) {
        if(graphNode.getSuccessors().isEmpty()) {
            res.add(graphNode);
        }
        for(GraphNode succ : graphNode.getSuccessors()) {
            if(!visited.contains(succ)) {
                visited.add(succ);
                this.leaves(succ, res);
            }
        }
    }

    public void addNodeToLeaves(GraphNode node) {
        this.visited = new ArrayList<>();
        this.visited.add(node);
        addNodeLeaves(this, node, null);
    }

    public void addNodeToLeaves(GraphNode node, GraphNode excludeNode) {
        this.visited = new ArrayList<>();
        this.visited.add(node);
        addNodeLeaves(this, node, excludeNode);
    }

    private void addNodeLeaves(GraphNode graphNode, GraphNode node, GraphNode excludeNode) {
        if(graphNode instanceof ExitNode || graphNode.equals(excludeNode)) {
            return;
        }
        if(graphNode.getSuccessors().size() == 0) {
            graphNode.addSuccessor(node);
        }
        for(GraphNode succ : graphNode.getSuccessors()) {
            if(!visited.contains(succ)) {
                visited.add(succ);
                this.addNodeLeaves(succ, node, excludeNode);
            }
        }
    }

    public void noOtherSuccessor(boolean bool) {
        this.noOtherSuccessor = bool;
    }
}
