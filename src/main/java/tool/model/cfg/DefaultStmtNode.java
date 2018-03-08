package tool.model.cfg;

public class DefaultStmtNode extends CFGNode {
    public DefaultStmtNode(String name) {
        super(name);
    }

    @Override
    public String getNodeType() {
        return "DefaultStmt";
    }
}
