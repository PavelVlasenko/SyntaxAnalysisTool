package tool.model.cfg;

public class WhileBeginNode extends CFGNode {
    public WhileBeginNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "WhileBegin";
    }
}
