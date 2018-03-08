package tool.model.cfg;

public class ExitNode extends CFGNode {
    public ExitNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "Exit";
    }
}
