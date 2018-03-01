package tool.visitors;

import org.antlr.v4.runtime.ParserRuleContext;
import tool.antlr4.CBaseListener;
import tool.antlr4.CParser;
import tool.ast.model.MethodNode;
import tool.ast.model.TreeNode;

public class CAstVisitor extends CBaseListener {

    private TreeNode ast;
    private TreeNode currentNode;

    public CAstVisitor(TreeNode ast) {
        this.ast = ast;
        currentNode = ast;
    }

    @Override
    public void enterFunctionSpecifier(CParser.FunctionSpecifierContext ctx) {
        super.enterFunctionSpecifier(ctx);
    }

    @Override
    public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        MethodNode methodNode = new MethodNode(ctx.declarator().getText());
        methodNode.setFilePath(currentNode.getFilePath());
        currentNode.addChild(methodNode);
        currentNode = methodNode;
    }

    @Override
    public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        currentNode = currentNode.getParent();
    }

    @Override
    public void enterForCondition(CParser.ForConditionContext ctx) {
        validate();
        ((MethodNode)currentNode).incrNbFor();
    }

    @Override
    public void enterDeclaration(CParser.DeclarationContext ctx) {
        validate();
        ((MethodNode)currentNode).incrNbLocalVar();
    }

    @Override
    public void enterConditionalExpression(CParser.ConditionalExpressionContext ctx) {
        validate();
        ((MethodNode)currentNode).incrNbIf();
    }

    @Override
    public void enterJumpStatement(CParser.JumpStatementContext ctx) {
        validate();
        ((MethodNode)currentNode).incrNbBreak();
    }

    @Override
    public void enterIterationStatement(CParser.IterationStatementContext ctx) {
        String iterationText = ctx.getText();
        validate();
        if(iterationText.contains("while")) {
            ((MethodNode)currentNode).incrNbWhile();
        }
        else if(iterationText.contains("for")) {
            ((MethodNode)currentNode).incrNbFor();
        }
    }

    @Override
    public void enterDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {
        validate();
        ((MethodNode)currentNode).setReturnType(ctx.getText());
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        super.enterEveryRule(ctx);
    }

    private void validate() {
        if(!(currentNode instanceof MethodNode)) {
            throw new RuntimeException("Error if statement location in " + currentNode.getNodeType());
        }
    }
}
