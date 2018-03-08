package tool.model.ast;

public class MethodNode extends ModifiersNode {
    protected String returnType;
    protected int nbIf;
    protected int nbSwitch;
    protected int nbFor;
    protected int nbWhile;
    protected int nbBreak;
    protected int nbContinue;
    protected int nbLocalVar;

    public MethodNode(String name) {
        super(name);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String getNodeType() {
        return "method";
    }

    public void incrNbIf() {
        nbIf++;
    }

    public void incrNbSwitch() {
        nbSwitch++;
    }

    public void incrNbFor() {
        nbFor++;
    }

    public void incrNbWhile() {
        nbWhile++;
    }

    public void incrNbBreak() {
        nbBreak++;
    }

    public void incrNbContinue() {
        nbContinue++;
    }

    public void incrNbLocalVar() {
        nbLocalVar++;
    }

    public int getNbIf() {
        return nbIf;
    }

    public int getNbSwitch() {
        return nbSwitch;
    }

    public int getNbFor() {
        return nbFor;
    }

    public int getNbWhile() {
        return nbWhile;
    }

    public int getNbBreak() {
        return nbBreak;
    }

    public int getNbContinue() {
        return nbContinue;
    }

    public int getNbLocalVar() {
        return nbLocalVar;
    }
}
