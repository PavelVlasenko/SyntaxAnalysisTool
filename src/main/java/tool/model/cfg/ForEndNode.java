package tool.model.cfg;

public class ForEndNode extends CFGNode {
    public ForEndNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "ForEnd";
    }
}
