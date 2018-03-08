package tool.model.cfg;

public class VarAssignNode extends VarNode {
    public VarAssignNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "VarAssign";
    }
}
