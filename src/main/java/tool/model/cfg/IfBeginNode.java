package tool.model.cfg;

public class IfBeginNode extends CFGNode {
    public IfBeginNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "IfBegin";
    }
}
