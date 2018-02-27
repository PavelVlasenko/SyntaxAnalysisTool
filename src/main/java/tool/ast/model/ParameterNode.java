package tool.ast.model;

public class ParameterNode extends TreeNode {
    protected String type;

    public ParameterNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "parameter";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
