package tool.model.ast;

import tool.model.TreeNode;

/**
 * Root node for AST model
 */
public class RootNode extends TreeNode {
    public RootNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "root";
    }
}
