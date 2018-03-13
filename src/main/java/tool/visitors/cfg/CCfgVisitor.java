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

    private LinkedList<GraphNode> conditions  = new LinkedList<>();

    private LinkedList<GraphNode> cycleBeginNodes = new LinkedList<>();
    private LinkedList<GraphNode> cycleEndNodes  = new LinkedList<>();

    private ArrayList<ParseTree> elseStatements = new ArrayList<>();
    private GraphNode currentNode;

    private int identifierGen = 0;

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
        if(!cycleEndNodes.isEmpty()) {
            condNode.addNodeToLeaves(ifEndNode, cycleEndNodes.getFirst());
        }
        else {
            condNode.addNodeToLeaves(ifEndNode);
        }
        condNode.addSuccessor(ifEndNode);
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
    public void enterJumpStatement(CParser.JumpStatementContext ctx) {
        String text = ctx.getText();
        if(text.startsWith("return")) {
            ReturnStmtNode returnStmtNode = new ReturnStmtNode(getId() + "Return");
            currentNode.addSuccessor(returnStmtNode);
            returnStmtNode.addSuccessor(exitNode);
        }
        else if(text.startsWith("break")) {
            BreakStmtNode breakStmtNode = new BreakStmtNode(getId() +"Break");
            currentNode.addSuccessor(breakStmtNode);
            breakStmtNode.addSuccessor(cycleEndNodes.getFirst());
            currentNode = conditions.getFirst();
        }
        else if(text.startsWith("continue")) {
            ContinueStmtNode continueStmtNode = new ContinueStmtNode(getId() + "Continue");
            currentNode.addSuccessor(continueStmtNode);
            continueStmtNode.addSuccessor(cycleBeginNodes.getFirst());
            GraphNode condNode = conditions.getFirst();
            currentNode = condNode;
        }

    }

    @Override
    public void enterIterationStatement(CParser.IterationStatementContext ctx) {
        String text = ctx.getText();
        GraphNode beginNode = null;
        GraphNode endNode = null;

        if(text.startsWith("while")) {
            beginNode = new WhileBeginNode("WhileBegin");
            endNode = new WhileEndNode(getId() + "WhileEnd");
        }
        else if(text.startsWith("for")) {
            beginNode = new ForBeginNode(getId() + "ForBegin");
            endNode = new ForEndNode(getId() + "ForEnd");
        }
        else if(text.startsWith("switch")) {
            beginNode = new ForBeginNode(getId() + "SwitchBegin");
            endNode = new ForEndNode(getId() + "SwitchEnd");
        }
        else if(text.startsWith("do")) {
            beginNode = new DoWhileBeginNode(getId() + "DoWhileBegin");
            endNode = new DoWhileEndNode(getId() + "DoWhileEnd");
        }
        else {
            LOGGER.warn("Unknown iteration statement");
        }
        ConditionNode condNode = new ConditionNode(getId() + "Condition");
        cycleBeginNodes.push(beginNode);
        conditions.push(condNode);
        currentNode.addSuccessor(beginNode);
        beginNode.addSuccessor(condNode);
        currentNode = condNode;

        cycleEndNodes.add(endNode);
    }

    @Override
    public void exitIterationStatement(CParser.IterationStatementContext ctx) {
        GraphNode beginNode = cycleBeginNodes.pop();
        GraphNode endNode = cycleEndNodes.pop();

        beginNode.addNodeToLeaves(beginNode, endNode);

        ConditionNode condNode = (ConditionNode) conditions.pop();
        condNode.addSuccessor(endNode);
        currentNode = endNode;
    }
}
