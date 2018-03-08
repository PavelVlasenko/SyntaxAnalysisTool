package tool.model.cfg;

public class VarDeclAssignNode extends VarNode {
    public VarDeclAssignNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "VarDeclAssignNode";
    }
}
