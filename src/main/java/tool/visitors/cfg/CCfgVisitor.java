package tool.visitors.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.CBaseListener;
import tool.antlr4.CParser;
import tool.antlr4.Python3Parser;
import tool.model.GraphNode;
import tool.model.cfg.*;

import java.sql.Statement;
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
//        GraphNode condNode = conditions.getFirst();
//        GraphNode ifEndNode = ifends.getFirst();
//
//        condNode.addNodeToLeaves(ifEndNode);
//        condNode.addSuccessor(ifEndNode);
//
//        conditions.pop();
//        ifends.pop();
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

//    public Object visit(SwitchStatement node, Object data) {
//        SwitchBeginNode switchBeginNode = new SwitchBeginNode("");
//        SwitchEndNode switchEndNode = new SwitchEndNode("");
//        switchEndNode.noOtherSuccessor(true);
//
//        switchBeginNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        switchBeginNode.setFilePath(currentFile);
//        switchEndNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        switchEndNode.setFilePath(currentFile);
//
//        entryNode.addNodeToLeaves(switchBeginNode);
//
//        continuables.push(switchBeginNode);
//        breakables.push(switchEndNode);
//        switchBreaked = false;
//
//        propagate(node, data);
//
//        continuables.pop();
//        breakables.pop();
//        switchBreaked = false;
//
//        switchEndNode.noOtherSuccessor(false);
//
//        return data;
//    }
//
//    // case
//    public Object visit(SwitchBlockStatementGroup node, Object data) {
//        GraphNode caseNode;
//        if(node.jjtGetFirstToken().toString() == "case")
//            caseNode = new CaseNode("");
//        else
//            caseNode = new DefaultStmtNode("");
//
//
//        caseNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        caseNode.setFilePath(currentFile);
//
//        if(switchBreaked)
//            continuables.getFirst().addSuccessor(caseNode);
//        else
//            entryNode.addNodeToLeaves(caseNode);
//
//        switchBreaked = false;
//
//        propagate(node, data);
//
//        return data;
//    }
//
//    public Object visit(WhileStatement node, Object data) {
//        WhileBeginNode whileBeginNode = new WhileBeginNode("");
//        ConditionNode condNode = new ConditionNode(this.readLine(node.jjtGetFirstToken().beginLine));
//        WhileEndNode whileEndNode = new WhileEndNode("");
//        whileEndNode.noOtherSuccessor(true);
//
//        whileBeginNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        whileBeginNode.setFilePath(currentFile);
//        condNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        condNode.setFilePath(currentFile);
//        whileEndNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        whileEndNode.setFilePath(currentFile);
//
//        condNode.setUsedVars(this.getUsedVar((SimpleNode) node.jjtGetChild(0)));
//
//        entryNode.addNodeToLeaves(whileBeginNode);
//        whileBeginNode.addSuccessor(condNode);
//
//        continuables.push(whileBeginNode);
//        breakables.push(whileEndNode);
//
//        propagate(node, condNode);
//
//        continuables.pop();
//        breakables.pop();
//
//        condNode.addNodeToLeaves(whileBeginNode);
//        condNode.addSuccessor(whileEndNode);
//
//        whileEndNode.noOtherSuccessor(false);
//
//        return whileEndNode;
//    }
//
//    public Object visit(DoStatement node, Object data) {
//        DoWhileBeginNode doWhileBeginNode = new DoWhileBeginNode("");
//        ConditionNode condNode = new ConditionNode("");
//        DoWhileEndNode doWhileEndNode = new DoWhileEndNode("");
//        doWhileEndNode.noOtherSuccessor(true);
//
//        doWhileBeginNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        doWhileBeginNode.setFilePath(currentFile);
//        condNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        condNode.setFilePath(currentFile);
//        doWhileEndNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        doWhileEndNode.setFilePath(currentFile);
//
//        entryNode.addNodeToLeaves(doWhileBeginNode);
//
//        continuables.push(doWhileBeginNode);
//        breakables.push(doWhileEndNode);
//
//        propagate(node, doWhileBeginNode);
//
//        continuables.pop();
//        breakables.pop();
//
//        doWhileBeginNode.addNodeToLeaves(condNode);
//        condNode.addSuccessor(doWhileBeginNode);
//        condNode.addSuccessor(doWhileEndNode);
//
//        doWhileEndNode.noOtherSuccessor(false);
//
//        return doWhileEndNode;
//    }
//
//    public Object visit(ForStatement node, Object data) {
//        ForBeginNode forBeginNode = new ForBeginNode("");
//        ConditionNode condNode = new ConditionNode("");
//        ForEndNode forEndNode = new ForEndNode("");
//        forEndNode.noOtherSuccessor(true);
//
//        forBeginNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        forBeginNode.setFilePath(currentFile);
//        condNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        condNode.setFilePath(currentFile);
//        forEndNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        forEndNode.setFilePath(currentFile);
//
//        entryNode.addNodeToLeaves(forBeginNode);
//        forBeginNode.addSuccessor(condNode);
//
//        continuables.push(forBeginNode);
//        breakables.push(forEndNode);
//
//        propagate(node, condNode);
//
//        continuables.pop();
//        breakables.pop();
//
//        condNode.addNodeToLeaves(forBeginNode);
//        condNode.addSuccessor(forEndNode);
//
//        forEndNode.noOtherSuccessor(false);
//
//        return forEndNode;
//    }
//
//    public Object visit(BreakStatement node, Object data) {
//        BreakStmtNode breakNode = new BreakStmtNode("");
//
//        breakNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        breakNode.setFilePath(currentFile);
//
//        entryNode.addNodeToLeaves(breakNode);
//        if(!breakables.isEmpty())
//            breakNode.addSuccessor(breakables.getFirst());
//        breakNode.noOtherSuccessor(true);
//
//        propagate(node, breakNode);
//
//        switchBreaked = true;
//
//        return data;
//    }
//
//    public Object visit(ContinueStatement node, Object data) {
//        ContinueStmtNode continueNode = new ContinueStmtNode("");
//        continueNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//        continueNode.setFilePath(currentFile);
//        entryNode.addNodeToLeaves(continueNode);
//        if(!continuables.isEmpty())
//            continueNode.addSuccessor(continuables.getFirst());
//        continueNode.noOtherSuccessor(true);
//
//        propagate(node, continueNode);
//
//        return continueNode;
//    }



    //@Override
    public void enterReturn_stmt(Python3Parser.Return_stmtContext ctx) {
        ReturnStmtNode returnNode = new ReturnStmtNode(getId() + "Return");
        returnNode.setLineNumber(1);//TODO
        returnNode.setFilePath(""); //TODO
        entryNode.addNodeToLeaves(returnNode);
        if(returnNode.getPredecessors().size() > 0) {
            returnNode.addSuccessor(exitNode);
            returnNode.noOtherSuccessor(true);
        }

        switchBreaked = true;
    }

//    public Object visit(VariableDeclarator node, Object data) {
//        if(node.jjtGetChild(1).jjtGetNumChildren() > 0) {
//            VarDeclAssignNode varDeclAssignNode = new VarDeclAssignNode(node.jjtGetFirstToken().toString());
//            varDeclAssignNode.setVarName(node.jjtGetFirstToken().toString());
//            varDeclAssignNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//            varDeclAssignNode.setFilePath(currentFile);
//            entryNode.addNodeToLeaves(varDeclAssignNode);
//
//            SimpleNode expression = (SimpleNode) node.jjtGetChild(1).jjtGetChild(0).jjtGetChild(0);
//            varDeclAssignNode.setUsedVars(this.getUsedVar(expression));
//        } else {
//            VarDeclNode varDeclNode = new VarDeclNode(node.jjtGetFirstToken().toString());
//            varDeclNode.setVarName(node.jjtGetFirstToken().toString());
//            varDeclNode.setLineNumber(node.jjtGetFirstToken().beginLine);
//            varDeclNode.setFilePath(currentFile);
//            entryNode.addNodeToLeaves(varDeclNode);
//        }
//
//
//        propagate(node, data);
//        return data;
//    }
}
