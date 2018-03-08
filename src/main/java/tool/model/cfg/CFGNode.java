package tool.model.cfg;

import tool.model.GraphNode;
import tool.model.TreeNode;

import java.util.HashSet;

public abstract class CFGNode extends GraphNode {
    protected TreeNode domNode = null;
    protected TreeNode postDomNode = null;
    protected GraphNode controlDependancyNode = null;
    protected GraphNode dataDependancyNode = null;
    protected GraphNode pdgNode = null;
    protected HashSet<String> usedVars = new HashSet<>();

    public HashSet<CFGNode> in = new HashSet<>();
    public HashSet<CFGNode> out = new HashSet<>();
    public HashSet<CFGNode> kill = new HashSet<>();
    public HashSet<CFGNode> gen = new HashSet<>();

    public CFGNode(String name) {
        super(name);
    }

    public TreeNode getDomNode() {
        if(domNode == null) {
            this.domNode = new TreeNode(this);
        }
        return domNode;
    }

    public TreeNode getPostDomNode() {
        if(postDomNode == null) {
            this.postDomNode = new TreeNode(this);
        }
        return postDomNode;
    }

    public GraphNode getControlDependancyNode() {
        if(controlDependancyNode == null) {
            this.controlDependancyNode = new GraphNode(this);
        }
        return controlDependancyNode;
    }

    public GraphNode getDataDependancyNode() {
        if(dataDependancyNode == null) {
            this.dataDependancyNode = new GraphNode(this);
        }
        return dataDependancyNode;
    }

    public GraphNode getPdgNode() {
        if(pdgNode == null) {
            this.pdgNode = new GraphNode(this);
        }
        return pdgNode;
    }

    public void setPdgNode(GraphNode pdgNode) {
        this.pdgNode = pdgNode;
    }

    public HashSet<String> getUsedVars() {
        return usedVars;
    }

    public void setUsedVars(HashSet<String> usedVars) {
        this.usedVars = usedVars;
    }
}
