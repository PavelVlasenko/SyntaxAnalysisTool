package tool.model.cfg;

public abstract class VarNode extends CFGNode{
    protected String varName;

    public VarNode(String name) {
        super(name);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }
}
