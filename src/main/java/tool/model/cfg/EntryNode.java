package tool.model.cfg;

public class EntryNode extends CFGNode {
    private ExitNode exitNode;

    public EntryNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "Entry";
    }

    public ExitNode getExitNode() {
        return exitNode;
    }

    public void setExitNode(ExitNode exitNode) {
        this.exitNode = exitNode;
    }
}
