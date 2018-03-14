package tool.model;

import java.io.Serializable;

public abstract class MyNode  implements Serializable {
    protected int id;
    protected String name;
    protected int lineNumber = -1;
    protected String filePath = null;
    protected String nodeType = "Unknown";

    private static int idCount = 0;

    public MyNode(String name) {
        this.id = MyNode.idCount;
        MyNode.idCount++;
        this.name = name;
    }

    public MyNode(MyNode node) {
        this(node.getName());
        this.setId(node.getId());
        this.setLineNumber(node.getLineNumber());
        this.setFilePath(node.getFilePath());
        this.setNodeType(node.getNodeType());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
}
