package tool.model.ast;

/**
 * Class node for AST model
 */
public class ClassNode extends InterfaceNode {

    protected String extension = null;

    public ClassNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "class";
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
