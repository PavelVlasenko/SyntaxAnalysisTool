package tool.visitors.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.antlr4.Python3BaseListener;
import tool.antlr4.Python3Parser;
import tool.model.cfg.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class PythonCfgVisitor extends Python3BaseListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PythonCfgVisitor.class);

    private HashMap<String, ArrayList<EntryNode>> cfgs = new HashMap<>();
    private LinkedList<PythonCfgMethodVisitor> visitors = new LinkedList<>();
    private PythonCfgMethodVisitor currentVisitor;

    public HashMap<String, ArrayList<EntryNode>> getCFGs() {
        return cfgs;
    }
    private String fileName;

    public PythonCfgVisitor(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void enterFuncdef(Python3Parser.FuncdefContext ctx) {
        if(currentVisitor != null) {
            visitors.push(currentVisitor);
        }
        currentVisitor = new PythonCfgMethodVisitor(cfgs, fileName);
        currentVisitor.enterFuncdef(ctx);
    }

    @Override
    public void exitFuncdef(Python3Parser.FuncdefContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.exitFuncdef(ctx);
        if (!visitors.isEmpty()){
            currentVisitor = visitors.pop();
        }
        else {
            currentVisitor = null;
        }
    }

    @Override
    public void enterIf_stmt(Python3Parser.If_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.enterIf_stmt(ctx);
    }

    @Override
    public void exitIf_stmt(Python3Parser.If_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.exitIf_stmt(ctx);
    }

    @Override
    public void enterExpr_stmt(Python3Parser.Expr_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.enterExpr_stmt(ctx);
    }

    @Override
    public void enterStmt(Python3Parser.StmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.enterStmt(ctx);
    }

    @Override
    public void enterBreak_stmt(Python3Parser.Break_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.enterBreak_stmt(ctx);
    }

    @Override
    public void enterContinue_stmt(Python3Parser.Continue_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.enterContinue_stmt(ctx);
    }

    @Override
    public void enterReturn_stmt(Python3Parser.Return_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.enterReturn_stmt(ctx);
    }

    @Override
    public void enterWhile_stmt(Python3Parser.While_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.enterWhile_stmt(ctx);
    }

    @Override
    public void exitWhile_stmt(Python3Parser.While_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.exitWhile_stmt(ctx);
    }

    @Override
    public void enterFor_stmt(Python3Parser.For_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.enterFor_stmt(ctx);
    }

    @Override
    public void exitFor_stmt(Python3Parser.For_stmtContext ctx) {
        if(isNotInMethod()) return;
        currentVisitor.exitFor_stmt(ctx);
    }

    private boolean isNotInMethod() {
        return currentVisitor == null;
    }
}