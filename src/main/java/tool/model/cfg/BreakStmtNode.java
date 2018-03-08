package tool.model.cfg;

public class BreakStmtNode extends CFGNode {
    public BreakStmtNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "BreakStmt";
    }
}
