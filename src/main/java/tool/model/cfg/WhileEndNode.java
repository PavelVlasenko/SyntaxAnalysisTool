package tool.model.cfg;

public class WhileEndNode extends CFGNode {
    public WhileEndNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "WhileEnd";
    }
}
