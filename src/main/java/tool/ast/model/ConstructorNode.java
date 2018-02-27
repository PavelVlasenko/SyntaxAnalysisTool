package tool.ast.model;

public class ConstructorNode extends MethodNode {
    public ConstructorNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "constructor";
    }

    @Override
    public String getReturnType() {
        return "";
    }
}
