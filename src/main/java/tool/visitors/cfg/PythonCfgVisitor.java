package tool.visitors.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.Python3BaseListener;
import tool.antlr4.Python3Parser;
import tool.model.GraphNode;
import tool.model.cfg.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class PythonCfgVisitor extends Python3BaseListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PythonCfgVisitor.class);
    private HashMap<String, ArrayList<EntryNode>> cfgs = new HashMap<>();
    private EntryNode entryNode;
    private ExitNode exitNode;
    private LinkedList<GraphNode> breakables;
    private LinkedList<GraphNode> continuables;
    private LinkedList<GraphNode> conditions;
    private LinkedList<GraphNode> ifends;
    private boolean switchBreaked = false;

    private int identifierGen = 0;

    private String getId() {
        String id = String.valueOf(identifierGen) + ".";
        identifierGen++;
        return id;
    }

    private void initNewCFG(String methodName) {
        String entryName = "Entry \n" + methodName;
        entryNode = new EntryNode(getId() + entryName);
        exitNode = new ExitNode(getId() + entryName);

        exitNode.noOtherSuccessor(true);

        entryNode.setExitNode(exitNode);
        exitNode.setLineNumber(entryNode.getLineNumber());
        exitNode.setFilePath("");

        breakables = new LinkedList<>();
        continuables = new LinkedList<>();
        conditions = new LinkedList<>();
        ifends = new LinkedList<>();
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
        IfEndNode ifEndNode = new IfEndNode(getId() + "IfEnd");

        entryNode.addNodeToLeaves(ifBeginNode);
        ifBeginNode.addSuccessor(condNode);

        conditions.push(condNode);
        ifends.push(ifEndNode);

        Python3Parser.SuiteContext suiteContext = ctx.getChild(Python3Parser.SuiteContext.class, 1);
    }

    @Override
    public void exitIf_stmt(Python3Parser.If_stmtContext ctx) {

    }

    @Override
    public void enterReturn_stmt(Python3Parser.Return_stmtContext ctx) {
        ReturnStmtNode returnNode = new ReturnStmtNode(getId() + "Return");
        entryNode.addNodeToLeaves(returnNode);
        if(returnNode.getPredecessors().size() > 0) {
            returnNode.addSuccessor(exitNode);
            returnNode.noOtherSuccessor(true);
        }

        switchBreaked = true;
    }

    @Override
    public void enterExpr_stmt(Python3Parser.Expr_stmtContext ctx) {
        String name = ctx.getText();
        String line = ctx.getText();

        if(line.matches("\\s*\\w*\\s*\\(.*")) {
            MethodCallNode methodCallNode = new MethodCallNode(getId() + "Method call");
            entryNode.addNodeToLeaves(methodCallNode);
        } else {
            VarAssignNode varAssignNode = new VarAssignNode(getId() + "Var declaration");
            varAssignNode.setVarName(name);
            entryNode.addNodeToLeaves(varAssignNode);
        }
    }
}