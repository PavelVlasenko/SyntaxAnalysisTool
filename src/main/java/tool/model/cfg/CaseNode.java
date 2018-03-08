package tool.model.cfg;

public class CaseNode extends CFGNode {
    public CaseNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "CaseStmt";
    }
}
