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

public class CfgVisitor extends Python3BaseListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CfgVisitor.class);
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
        entryNode.setLineNumber(1); //TODO
        entryNode.setFilePath(""); //TODO
        exitNode.setLineNumber(entryNode.getLineNumber());
        exitNode.setFilePath(""); //TODO

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
        ConditionNode condNode = new ConditionNode(getId() +"Condition");//TODO
        IfEndNode ifEndNode = new IfEndNode(getId() + "IfEnd");

        ifBeginNode.setLineNumber(1); //TODO
        ifBeginNode.setFilePath(""); //TODO
        condNode.setLineNumber(1); //TODO
        condNode.setFilePath("");
        ifEndNode.setLineNumber(1);
        ifEndNode.setFilePath("");

        //condNode.setUsedVars(this.getUsedVar((SimpleNode) node.jjtGetChild(0)));

        entryNode.addNodeToLeaves(ifBeginNode);
        ifBeginNode.addSuccessor(condNode);

        conditions.push(condNode);
        ifends.push(ifEndNode);

        Python3Parser.SuiteContext suiteContext = ctx.getChild(Python3Parser.SuiteContext.class, 1);
//        if(suiteContext != null) {
//            enterElseStatement(suiteContext);
//        }
    }

//    public void enterElseStatement(Python3Parser.SuiteContext ctx) {
//        GraphNode condNode = conditions.getFirst();
//        GraphNode ifEndNode = ifends.getFirst();
//
//        GraphNode trueCaseSubGraph = condNode.getSuccessors().get(0);
//        condNode.removeSuccessor(0);
//
//        if(condNode.getSuccessors().isEmpty()) {
//            condNode.addSuccessor(ifEndNode);
//        }
//
//        condNode.addSuccessor(0, trueCaseSubGraph);
//        condNode.addNodeToLeaves(ifEndNode);
//
//        conditions.pop();
//        ifends.pop();
//    }


    @Override
    public void enterSuite(Python3Parser.SuiteContext ctx) {
        super.enterSuite(ctx);
    }



    @Override
    public void exitIf_stmt(Python3Parser.If_stmtContext ctx) {
        GraphNode condNode = conditions.getFirst();
        GraphNode ifEndNode = ifends.getFirst();

        condNode.addNodeToLeaves(ifEndNode);
        condNode.addSuccessor(ifEndNode);

        conditions.pop();
        ifends.pop();
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


    @Override
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


//    @Override
//    public void enterSimple_stmt(Python3Parser.Simple_stmtContext ctx) {
//        super.enterSimple_stmt(ctx);
//    }

    @Override
    public void enterExpr_stmt(Python3Parser.Expr_stmtContext ctx) {
        String name = ctx.getText();
//        if(name.equals("++") || name.equals("--")) {
//            name = node.jjtGetFirstToken().next.toString();
//        }

//        String line = readLine(node.jjtGetFirstToken().beginLine);
        String line = ctx.getText();

        if(line.matches("\\s*\\w*\\s*\\(.*")) {
            //MethodCallNode methodCallNode = new MethodCallNode(line);
            MethodCallNode methodCallNode = new MethodCallNode(getId() + "Method call");
            methodCallNode.setLineNumber(1); //TODO
            methodCallNode.setFilePath(""); //TODO
           //HashSet<String> usedVars = this.getUsedVar((SimpleNode) node.jjtGetChild(0));
           // usedVars.remove(name);
          //  methodCallNode.setUsedVars(usedVars);

            entryNode.addNodeToLeaves(methodCallNode);
        } else {
            //VarAssignNode varAssignNode = new VarAssignNode(line);
            VarAssignNode varAssignNode = new VarAssignNode(getId() + "Var declaration");
            varAssignNode.setVarName(name);
            varAssignNode.setLineNumber(1); //TODO
            varAssignNode.setFilePath(""); //TODO

//            HashSet<String> usedVars = this.getUsedVar((SimpleNode) node.jjtGetChild(0));
//            if(!line.matches(".*=.*"+name+".*")) {
//                usedVars.remove(name);
//            }
//            varAssignNode.setUsedVars(usedVars);

            entryNode.addNodeToLeaves(varAssignNode);
        }
    }

//    @Override
//    public void enterSmall_stmt(Python3Parser.Small_stmtContext ctx) {
//        super.enterSmall_stmt(ctx);
//    }



    @Override
    public void enterStmt(Python3Parser.StmtContext ctx) {
        /*
        String name = ctx.getText();
//        if(name.equals("++") || name.equals("--")) {
//            name = node.jjtGetFirstToken().next.toString();
//        }

//        String line = readLine(node.jjtGetFirstToken().beginLine);
        String line = ctx.getText();

        if(line.matches("\\s*\\w*\\s*\\(.*")) {
            MethodCallNode methodCallNode = new MethodCallNode(line);
            methodCallNode.setLineNumber(1); //TODO
            methodCallNode.setFilePath(""); //TODO
           //HashSet<String> usedVars = this.getUsedVar((SimpleNode) node.jjtGetChild(0));
           // usedVars.remove(name);
          //  methodCallNode.setUsedVars(usedVars);

            entryNode.addNodeToLeaves(methodCallNode);
        } else {
            VarAssignNode varAssignNode = new VarAssignNode(line);
            varAssignNode.setVarName(name);
            varAssignNode.setLineNumber(1); //TODO
            varAssignNode.setFilePath(""); //TODO

//            HashSet<String> usedVars = this.getUsedVar((SimpleNode) node.jjtGetChild(0));
//            if(!line.matches(".*=.*"+name+".*")) {
//                usedVars.remove(name);
//            }
//            varAssignNode.setUsedVars(usedVars);

            entryNode.addNodeToLeaves(varAssignNode);
        }*/
    }

//    private String readLine(int lineNumber) {
//        String line = FileManager.readLine(currentFile, lineNumber);
//        line = line.replaceAll("\\s", "");
//        line = line.replace("\\", "\\\\");
//        line = line.replace("{", "");
//        line = line.replace("}", "");
//        line = line.replace("\"", "\\\"");
//        line = line.replace("while", "");
//        line = line.replace("if", "");
//        line = line.replace("else", "");
//
//        return line;
//    }

//    private HashSet<String> getUsedVar(SimpleNode node) {
//        HashSet<String> res = new HashSet<>();
//        if(node.toString().contains("Expression") || node.toString().contains("Primary") || node.toString().contains("Arguments") || node.toString().contains("IdentifierSuffix")) {
//            for(int i = 0; i < node.jjtGetNumChildren(); i++) {
//                res.addAll(this.getUsedVar((SimpleNode) node.jjtGetChild(i)));
//            }
//        } else if(node.toString().equals("Identifier")) {
//            res.add(node.jjtGetFirstToken().toString());
//        }
//
//        return res;
//    }
}
