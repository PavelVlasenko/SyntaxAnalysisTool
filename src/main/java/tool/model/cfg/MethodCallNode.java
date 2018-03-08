package tool.model.cfg;

public class MethodCallNode extends CFGNode {
    public MethodCallNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "MethodCall";
    }
}
