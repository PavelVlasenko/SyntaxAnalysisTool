package tool.visitors.ast;

import org.antlr.v4.runtime.tree.ParseTree;
import tool.antlr4.Python3BaseListener;
import tool.antlr4.Python3Parser;
import tool.model.ast.ClassNode;
import tool.model.ast.ConstructorNode;
import tool.model.ast.FieldNode;
import tool.model.ast.MethodNode;
import tool.model.TreeNode;
import tool.model.cfg.MethodCallNode;
import tool.model.cfg.VarAssignNode;

public class PythonAstVisitor extends Python3BaseListener {

    private static final String CONSTRUCTOR_METHOD = "__init__";

    private TreeNode ast;
    private TreeNode currentNode;

    private boolean inClass = false;
    private boolean inMethod = false;

    public PythonAstVisitor(TreeNode ast) {
        this.ast = ast;
        currentNode = ast;
    }

    @Override
    public void enterClassdef(Python3Parser.ClassdefContext ctx) {
        ClassNode classNode = new ClassNode(ctx.NAME().getText());
        ParseTree extendClass = ctx.getChild(Python3Parser.ArglistContext.class, 0);
        if(extendClass != null) {
            classNode.setExtension(extendClass.getText());
        }

        classNode.setFilePath(currentNode.getFilePath());
        currentNode.addChild(classNode);
        currentNode = classNode;
        inClass = true;
    }

    @Override
    public void exitClassdef(Python3Parser.ClassdefContext ctx) {
        currentNode = currentNode.getParent();
        inClass = false;
    }

    @Override
    public void enterFuncdef(Python3Parser.FuncdefContext ctx) {
        String methodName = ctx.NAME().getText();
        TreeNode node;
        if(CONSTRUCTOR_METHOD.equals(methodName)) {
            node = new ConstructorNode(methodName);
        }
        else {
            node = new MethodNode(methodName);
        }
        node.setFilePath(currentNode.getFilePath());
        currentNode.addChild(node);
        currentNode = node;
        inMethod = true;
    }

    @Override
    public void exitFuncdef(Python3Parser.FuncdefContext ctx) {
        currentNode = currentNode.getParent();
        inMethod = false;
    }

    @Override
    public void enterExpr_stmt(Python3Parser.Expr_stmtContext ctx) {
        String line = ctx.getText();
        if(inClass && !inMethod && !line.matches("\\s*\\w*\\s*\\(.*") && !line.contains("\"\"\"")) {
            String text = null;
            if(line.contains("=")) {
                text = line.substring(0, line.indexOf("="));
            }
            else {
                text = line;
            }
            FieldNode fieldNode = new FieldNode(text);
            currentNode.addChild(fieldNode);
        }
    }

    @Override
    public void enterIf_stmt(Python3Parser.If_stmtContext ctx) {
        validate();
        ((MethodNode)currentNode).incrNbIf();
    }

    @Override
    public void enterWhile_stmt(Python3Parser.While_stmtContext ctx) {
        validate();
        ((MethodNode)currentNode).incrNbWhile();
    }

    @Override
    public void enterFor_stmt(Python3Parser.For_stmtContext ctx) {
         validate();
        ((MethodNode)currentNode).incrNbFor();
    }

    private void validate() {
        if(!(currentNode instanceof MethodNode)) {
            throw new RuntimeException("Error if statement location in " + currentNode.getNodeType());
        }
    }

}
