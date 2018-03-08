package tool.model.cfg;

public class SwitchEndNode extends CFGNode {
    public SwitchEndNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "SwitchEnd";
    }
}
