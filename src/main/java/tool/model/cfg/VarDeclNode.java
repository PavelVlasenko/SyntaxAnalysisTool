package tool.model.cfg;

public class VarDeclNode extends VarNode {
    public VarDeclNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "VarDecl";
    }
}
