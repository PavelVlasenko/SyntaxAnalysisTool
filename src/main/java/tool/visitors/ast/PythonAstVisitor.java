package tool.visitors.ast;

import tool.antlr4.Python3BaseListener;
import tool.antlr4.Python3Parser;
import tool.model.ast.ClassNode;
import tool.model.ast.ConstructorNode;
import tool.model.ast.MethodNode;
import tool.model.TreeNode;

public class PythonAstVisitor extends Python3BaseListener {

    private static final String CONSTRUCTOR_METHOD = "__init__";

    private TreeNode ast;
    private TreeNode currentNode;

    public PythonAstVisitor(TreeNode ast) {
        this.ast = ast;
        currentNode = ast;
    }

    @Override
    public void enterClassdef(Python3Parser.ClassdefContext ctx) {
        ClassNode classNode = new ClassNode(ctx.NAME().getText());
        classNode.setFilePath(currentNode.getFilePath());
        currentNode.addChild(classNode);
        currentNode = classNode;
    }

    @Override
    public void exitClassdef(Python3Parser.ClassdefContext ctx) {
        currentNode = currentNode.getParent();
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
    }

    @Override
    public void exitFuncdef(Python3Parser.FuncdefContext ctx) {
        currentNode = currentNode.getParent();
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
