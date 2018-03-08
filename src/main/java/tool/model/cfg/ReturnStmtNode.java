package tool.model.cfg;

public class ReturnStmtNode extends CFGNode {
    public ReturnStmtNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "ReturnStmt";
    }
}
