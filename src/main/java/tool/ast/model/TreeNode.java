package tool.ast.model;

import java.util.ArrayList;

public class TreeNode extends MyNode {
    protected TreeNode parent;
    protected ArrayList<TreeNode> children;


    public TreeNode(String name) {
        super(name);
        this.children = new ArrayList<>();
    }

    public TreeNode(MyNode node) {
        super(node);
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        child.parent = this;
        this.children.add(child);
    }

    public void addFirstChild(TreeNode child) {
        child.parent = this;
        this.children.add(0, child);
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        if(this.parent != null) {
            this.parent.getChildren().remove(this);
        }
        this.parent = parent;
        this.parent.addChild(this);
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public TreeNode getPreviousSibling() {
        if(getParent() == null || getParent().getChildren().indexOf(this) == 0) {
            return null;
        } else {
            int index = getParent().getChildren().indexOf(this) - 1;
            return getParent().getChildren().get(index);
        }
    }

    @Override
    public int getLineNumber() {
        if(lineNumber == -1 && getParent() != null)
            return getParent().getLineNumber();
        else
            return lineNumber;
    }

    @Override
    public String getFilePath() {
        if(filePath == null && getParent() != null)
            return getParent().getFilePath();
        else
            return filePath;
    }
}
