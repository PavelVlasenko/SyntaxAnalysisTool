package tool.model.cfg;

public class IfEndNode extends CFGNode {
    public IfEndNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "IfEnd";
    }
}
