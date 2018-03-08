package tool.metrics;

import tool.model.ast.MethodNode;
import tool.model.TreeNode;

import java.util.List;

/**
 * Creates metrics in CSV
 */
public class MetricsConverter {

    private static final String DELIMETER = ";";
    private static final String END_OF_ROW ="\r\n";

    private StringBuilder metrics = new StringBuilder();

    /**
     * Metrics format:
     * Method name; Class name; Number if loops; Number of while loops;
     * Number of For loops; Number switch/case;
     *
     */
    public String getMetricsInCsv(List<TreeNode> ast) {
        for(TreeNode rootNode : ast) {
            processNode(rootNode);
        }
        return metrics.toString();
    }

    private void processNode(TreeNode node) {
        if (node.getNodeType().equals("method")) {
            MethodNode methodNode = (MethodNode) node;
            metrics.append(methodNode.getName());
            metrics.append(DELIMETER);
            metrics.append(methodNode.getParent().getName());
            metrics.append(DELIMETER);
            metrics.append(methodNode.getNbIf());
            metrics.append(DELIMETER);
            metrics.append(methodNode.getNbWhile());
            metrics.append(DELIMETER);
            metrics.append(methodNode.getNbFor());
            metrics.append(DELIMETER);
            metrics.append(methodNode.getNbSwitch());
            metrics.append(END_OF_ROW);
        }
        else {
            for (TreeNode childNode : node.getChildren()) {
                processNode(childNode);
            }
        }
    }


}
