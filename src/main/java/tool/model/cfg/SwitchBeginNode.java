package tool.model.cfg;

public class SwitchBeginNode extends CFGNode {
    public SwitchBeginNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "SwitchBegin";
    }
}
