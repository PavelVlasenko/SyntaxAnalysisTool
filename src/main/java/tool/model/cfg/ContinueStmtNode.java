package tool.model.cfg;

public class ContinueStmtNode extends CFGNode {
    public ContinueStmtNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "ContinueStmt";
    }
}
