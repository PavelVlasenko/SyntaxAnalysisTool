package tool.visitors.cfg;

import org.antlr.v4.runtime.tree.ParseTree;
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
    private ArrayList<ParseTree> elseStatements = new ArrayList<>();
    private LinkedList<GraphNode> cycleNodes = new LinkedList<>();

    private GraphNode currentNode;

    private String getId() {
        String id = String.valueOf(identifierGen) + ".";
        identifierGen++;
        return id;
    }

    private void initNewCFG(String methodName) {
        String entryName = "Entry \n" + methodName;
        entryNode = new EntryNode(getId() + entryName);
        entryNode.setFilePath(""); //TODO
        entryNode.setMethodName(methodName);
        currentNode = entryNode;

        exitNode = new ExitNode(getId() + "Exit \nmain");
        entryNode.setExitNode(exitNode);
    }

    private void endNewCFG() {
        cfgs.put(entryNode.getFilePath(), new ArrayList<>());
        entryNode.addNodeToLeaves(exitNode);
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
        conditions.push(condNode);

        currentNode.addNodeToLeaves(ifBeginNode);
        ifBeginNode.addSuccessor(condNode);
        currentNode = condNode;

        int childCount = ctx.getChildCount();
        for(int i = 0; i < childCount; i++) {
            String nodeName = ctx.getChild(i).getText();
            if("else".equals(nodeName) ||
                    "else if".equals(nodeName)) {
                if(childCount > (i + 1)) {
                    elseStatements.add(ctx.getChild(i + 1));
                }
            }
        }

    }

    @Override
    public void exitSelectionStatement(CParser.SelectionStatementContext ctx) {
        IfEndNode ifEndNode = new IfEndNode(getId() + "IfEnd");
        GraphNode condNode = conditions.pop();
        condNode.addNodeToLeaves(ifEndNode);
        currentNode = ifEndNode;
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
            currentNode.addSuccessor(methodCallNode);
        }
    }

    @Override
    public void enterDeclaration(CParser.DeclarationContext ctx) {
        String line = ctx.getText();
        if(line.matches("\\s*\\w*\\s*\\(.*")) {
            MethodCallNode methodCallNode = new MethodCallNode(getId() + "Method call");
            currentNode.addSuccessor(methodCallNode);
            currentNode = methodCallNode;
        } else {
            VarAssignNode varAssignNode = new VarAssignNode(getId() + "Var declaration");
            currentNode.addSuccessor(varAssignNode);
            currentNode = varAssignNode;
        }
    }

    @Override
    public void enterStatement(CParser.StatementContext ctx) {
        if(elseStatements.contains(ctx)) {
            GraphNode condNode = conditions.getFirst();
            currentNode = condNode;
        }
    }

    @Override
    public void enterIterationStatement(CParser.IterationStatementContext ctx) {
        String text = ctx.getText();
        GraphNode beginNode = null;

        if(text.startsWith("while")) {
            beginNode = new WhileBeginNode("WhileBegin");
        }
        else if(text.startsWith("for")) {
            beginNode = new ForBeginNode(getId() + "ForBegin");
        }
        ConditionNode condNode = new ConditionNode(getId() + "Condition");
        cycleNodes.push(beginNode);
        conditions.push(condNode);
        currentNode.addSuccessor(beginNode);
        beginNode.addSuccessor(condNode);
        currentNode = condNode;
    }

    @Override
    public void exitIterationStatement(CParser.IterationStatementContext ctx) {
        String text = ctx.getText();
        GraphNode endNode = null;
        if(text.startsWith("while")) {
            endNode = new WhileEndNode(getId() + "WhileEnd");
        }
        else if(text.startsWith("for")) {
            endNode = new ForEndNode(getId() + "ForEnd");
        }
        GraphNode iterationBeginNode = cycleNodes.pop();
        iterationBeginNode.addNodeToLeaves(iterationBeginNode);

        ConditionNode condNode = (ConditionNode) conditions.pop();
        condNode.addSuccessor(endNode);
        currentNode = endNode;
    }
}
