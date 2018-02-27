package tool.ast.model;

public class RootNode extends TreeNode {
    public RootNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "root";
    }
}
