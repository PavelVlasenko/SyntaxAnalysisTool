package tool.model.cfg;

public class DoWhileEndNode extends CFGNode {
    public DoWhileEndNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "DoWhileEnd";
    }
}
