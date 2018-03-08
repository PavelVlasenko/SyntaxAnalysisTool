package tool.model.cfg;

public class ConditionNode extends CFGNode {
    public ConditionNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "Condition";
    }
}
