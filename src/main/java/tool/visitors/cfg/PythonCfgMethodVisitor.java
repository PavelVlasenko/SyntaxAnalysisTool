package tool.visitors.cfg;

import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.Python3BaseListener;
import tool.antlr4.Python3Parser;
import tool.model.GraphNode;
import tool.model.cfg.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class PythonCfgMethodVisitor extends Python3BaseListener {

    private HashMap<String, ArrayList<EntryNode>> cfgs;

    private static final Logger LOGGER = LoggerFactory.getLogger(PythonCfgVisitor.class);

    private EntryNode entryNode;
    private ExitNode exitNode;

    private LinkedList<GraphNode> conditions  = new LinkedList<>();

    private LinkedList<GraphNode> cycleBeginNodes = new LinkedList<>();
    private LinkedList<GraphNode> cycleEndNodes  = new LinkedList<>();

    private ArrayList<ParseTree> elseStatements = new ArrayList<>();
    private GraphNode currentNode;
    private String fileName;

    private int identifierGen = 0;

    public PythonCfgMethodVisitor(HashMap<String, ArrayList<EntryNode>> cfgs, String fileName) {
        this.cfgs = cfgs;
        this.fileName = fileName;
    }

    private String getId() {
        String id = String.valueOf(identifierGen) + ".";
        identifierGen++;
        return id;
    }

    private void initNewCFG(String methodName) {
        String entryName = "Entry \n" + methodName;
        entryNode = new EntryNode(getId() + entryName);
        entryNode.setFilePath(fileName);
        entryNode.setMethodName(methodName);
        currentNode = entryNode;

        exitNode = new ExitNode(getId() + "Exit \nmain");
        entryNode.setExitNode(exitNode);
    }

    private void endNewCFG() {
        if(!cfgs.containsKey(entryNode.getFilePath())) {
            cfgs.put(entryNode.getFilePath(), new ArrayList<>());
        }
        entryNode.addNodeToLeaves(exitNode);
        cfgs.get(entryNode.getFilePath()).add(entryNode);
    }

    public HashMap<String, ArrayList<EntryNode>> getCFGs() {
        return cfgs;
    }

    @Override
    public void enterFuncdef(Python3Parser.FuncdefContext ctx) {
        String text = ctx.getText();
        String methodName = text.substring(0, text.indexOf("("));
        LOGGER.info("Enter func def {}", methodName);
        initNewCFG(methodName);
    }

    @Override
    public void exitFuncdef(Python3Parser.FuncdefContext ctx) {
        LOGGER.info("Exit func def");
        endNewCFG();
    }

    @Override
    public void enterIf_stmt(Python3Parser.If_stmtContext ctx) {
        IfBeginNode ifBeginNode = new IfBeginNode(getId() + "IfBegin");
        ConditionNode condNode = new ConditionNode(getId() +"Condition");
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
    public void exitIf_stmt(Python3Parser.If_stmtContext ctx) {
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
    public void enterExpr_stmt(Python3Parser.Expr_stmtContext ctx) {
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
    public void enterStmt(Python3Parser.StmtContext ctx) {
        if(elseStatements.contains(ctx)) {
            GraphNode condNode = conditions.getFirst();
            currentNode = condNode;
        }
    }

    @Override
    public void enterBreak_stmt(Python3Parser.Break_stmtContext ctx) {
        BreakStmtNode breakStmtNode = new BreakStmtNode(getId() +"Break");
        currentNode.addSuccessor(breakStmtNode);
        breakStmtNode.addSuccessor(cycleEndNodes.getFirst());
        currentNode = conditions.getFirst();
    }

    @Override
    public void enterContinue_stmt(Python3Parser.Continue_stmtContext ctx) {
        ContinueStmtNode continueStmtNode = new ContinueStmtNode(getId() + "Continue");
        currentNode.addSuccessor(continueStmtNode);
        continueStmtNode.addSuccessor(cycleBeginNodes.getFirst());
        GraphNode condNode = conditions.getFirst();
        currentNode = condNode;
    }

    @Override
    public void enterReturn_stmt(Python3Parser.Return_stmtContext ctx) {
        ReturnStmtNode returnStmtNode = new ReturnStmtNode(getId() + "Return");
        currentNode.addSuccessor(returnStmtNode);
        returnStmtNode.addSuccessor(exitNode);
    }

    @Override
    public void enterWhile_stmt(Python3Parser.While_stmtContext ctx) {
        GraphNode beginNode = new WhileBeginNode("WhileBegin");
        GraphNode endNode = new WhileEndNode(getId() + "WhileEnd");
        enterIterationStatement(beginNode, endNode);
    }

    @Override
    public void exitWhile_stmt(Python3Parser.While_stmtContext ctx) {
        exitIterationStatement();
    }

    @Override
    public void enterFor_stmt(Python3Parser.For_stmtContext ctx) {
        GraphNode beginNode = new ForBeginNode(getId() + "ForBegin");
        GraphNode endNode = new ForEndNode(getId() + "ForEnd");
        enterIterationStatement(beginNode, endNode);
    }

    @Override
    public void exitFor_stmt(Python3Parser.For_stmtContext ctx) {
        exitIterationStatement();
    }

    private void enterIterationStatement(GraphNode beginNode, GraphNode endNode) {
        ConditionNode condNode = new ConditionNode(getId() + "Condition");
        cycleBeginNodes.push(beginNode);
        conditions.push(condNode);
        currentNode.addSuccessor(beginNode);
        beginNode.addSuccessor(condNode);
        currentNode = condNode;

        cycleEndNodes.add(endNode);
    }

    private void exitIterationStatement() {
        GraphNode beginNode = cycleBeginNodes.pop();
        GraphNode endNode = cycleEndNodes.pop();

        beginNode.addNodeToLeaves(beginNode, endNode);

        ConditionNode condNode = (ConditionNode) conditions.pop();
        condNode.addSuccessor(endNode);
        currentNode = endNode;
    }
}
