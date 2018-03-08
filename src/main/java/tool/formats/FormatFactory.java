package tool.formats;

import tool.model.MyNode;
import tool.model.TreeNode;

public abstract class FormatFactory {

    public abstract AbstractFormat getFormat(MyNode node);

    public String format(TreeNode node) {
        AbstractFormat format = this.getFormat(node);
        String result = format.getString(0);

        for(TreeNode child : node.getChildren()) {
            result += this.format(child);
        }

        result += format.getString(1);

        return result;
    }
}
