package tool.visitors.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.CBaseListener;
import tool.antlr4.CParser;
import tool.model.GraphNode;
import tool.model.cfg.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class CCfgVisitor extends CBaseListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CCfgVisitor.class);
    private HashMap<String, ArrayList<EntryNode>> cfgs = new HashMap<>();
    private EntryNode entryNode;
    private ExitNode exitNode;
    private LinkedList<GraphNode> breakables  = new LinkedList<>();
    private LinkedList<GraphNode> continuables  = new LinkedList<>();
    private LinkedList<GraphNode> conditions  = new LinkedList<>();
    private LinkedList<GraphNode> ifends  = new LinkedList<>();
    private boolean switchBreaked = false;

    private int identifierGen = 0;
    private LinkedList<ConditionNode> conditionNodes = new LinkedList<>();
    private ArrayList<CParser.StatementContext> elseStatements = new ArrayList<>();

    private String getId() {
        String id = String.valueOf(identifierGen) + ".";
        identifierGen++;
        return id;
    }

    private void initNewCFG(String methodName) {
        String entryName = "Entry \n" + methodName;
        entryNode = new EntryNode(getId() + entryName);
        entryNode.setFilePath(""); //TODO
        exitNode = new ExitNode(getId() + entryName);
        exitNode.noOtherSuccessor(true);
        entryNode.setExitNode(exitNode);
    }

    private void endNewCFG() {
        if(!cfgs.containsKey(entryNode.getFilePath())) {
            cfgs.put(entryNode.getFilePath(), new ArrayList<>());
        }
        entryNode.addNodeToLeaves(exitNode);
        entryNode.addSuccessor(exitNode);
        cfgs.get(entryNode.getFilePath()).add(entryNode);
    }

    public HashMap<String, ArrayList<EntryNode>> getCFGs() {
        return cfgs;
    }

    @Override
    public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        String text = ctx.getText();
        String methodName = text.substring(0, text.indexOf("("));
        LOGGER.info("Enter func def {}", methodName);
        initNewCFG(methodName);
    }

    @Override
    public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        LOGGER.info("Exit func def");
        endNewCFG();
    }

    @Override
    public void enterSelectionStatement(CParser.SelectionStatementContext ctx) {
        IfBeginNode ifBeginNode = new IfBeginNode(getId() + "IfBegin");
        ConditionNode condNode = new ConditionNode(getId() +"Condition");//TODO
        IfEndNode ifEndNode = new IfEndNode(getId() + "IfEnd");
        entryNode.addNodeToLeaves(ifBeginNode);
        ifBeginNode.addSuccessor(condNode);

        conditions.push(condNode);
        ifends.push(ifEndNode);

    }

    @Override
    public void exitSelectionStatement(CParser.SelectionStatementContext ctx) {

    }

    @Override
    public void enterAssignmentOperator(CParser.AssignmentOperatorContext ctx) {
        super.enterAssignmentOperator(ctx);
    }

    @Override
    public void enterExpression(CParser.ExpressionContext ctx) {
        if(ctx.getText().matches("\\s*\\w*\\s*\\(.*")) {
            String line = ctx.getText();
            MethodCallNode methodCallNode = new MethodCallNode(getId() + "Method call");
            entryNode.addNodeToLeaves(methodCallNode);
        }
    }

    @Override
    public void enterDeclaration(CParser.DeclarationContext ctx) {
        String line = ctx.getText();
        if(line.matches("\\s*\\w*\\s*\\(.*")) {
            MethodCallNode methodCallNode = new MethodCallNode(getId() + "Method call");
            entryNode.addNodeToLeaves(methodCallNode);
        } else {
            VarAssignNode varAssignNode = new VarAssignNode(getId() + "Var declaration");
            entryNode.addNodeToLeaves(varAssignNode);
        }
    }

    @Override
    public void enterStatement(CParser.StatementContext ctx) {
        if(elseStatements.contains(ctx)) {
            GraphNode condNode = conditions.getFirst();
            GraphNode ifEndNode = ifends.getFirst();

            GraphNode trueCaseSubGraph = condNode.getSuccessors().get(0);
            condNode.removeSuccessor(0);

            if(condNode.getSuccessors().isEmpty()) {
                condNode.addSuccessor(ifEndNode);
            }

            condNode.addSuccessor(0, trueCaseSubGraph);
            condNode.addNodeToLeaves(ifEndNode);

            conditions.pop();
            ifends.pop();
        }
    }

    @Override
    public void enterForDeclaration(CParser.ForDeclarationContext ctx) {
        ForBeginNode forBeginNode = new ForBeginNode(getId() + "ForBegin");
        ConditionNode condNode = new ConditionNode(getId() + "Condition");
        conditionNodes.push(condNode);

        ForEndNode forEndNode = new ForEndNode(getId() + "ForEnd");
        forEndNode.noOtherSuccessor(true);

        entryNode.addNodeToLeaves(forBeginNode);
        forBeginNode.addSuccessor(condNode);

        continuables.push(forBeginNode);
        breakables.push(forEndNode);
    }

    @Override
    public void exitForDeclaration(CParser.ForDeclarationContext ctx) {
        ForBeginNode forBeginNode = (ForBeginNode) continuables.pop();
        ForEndNode forEndNode = (ForEndNode) breakables.pop();

        ConditionNode condNode = conditionNodes.pop();
        condNode.addNodeToLeaves(forBeginNode);
        condNode.addSuccessor(forEndNode);

        forEndNode.noOtherSuccessor(false);
    }

    @Override
    public void enterForCondition(CParser.ForConditionContext ctx) {
        super.enterForCondition(ctx);
    }

    @Override
    public void enterForExpression(CParser.ForExpressionContext ctx) {
        super.enterForExpression(ctx);
    }
}
