package tool.model.cfg;

public class ForBeginNode extends CFGNode {
    public ForBeginNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "ForBegin";
    }
}
