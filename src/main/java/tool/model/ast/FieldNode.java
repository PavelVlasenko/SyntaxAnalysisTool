package tool.model.ast;

public class FieldNode extends ClassNode {
    protected String type;

    public FieldNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "field";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
