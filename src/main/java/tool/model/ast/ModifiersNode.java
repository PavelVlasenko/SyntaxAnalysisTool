package tool.model.ast;

import tool.model.TreeNode;

/**
 * Modifier node for AST model
 */
public abstract class ModifiersNode extends TreeNode {
    protected boolean isStatic = false;
    protected String visibility = "package-private";
    protected boolean isAbstract = false;

    public ModifiersNode(String name) {
        super(name);
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }
}
