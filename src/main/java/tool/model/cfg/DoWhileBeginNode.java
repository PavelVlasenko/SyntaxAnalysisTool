package tool.model.cfg;

public class DoWhileBeginNode extends CFGNode {
    public DoWhileBeginNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "DoWhileBegin";
    }
}
