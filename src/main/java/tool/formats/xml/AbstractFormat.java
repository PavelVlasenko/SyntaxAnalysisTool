package tool.formats.xml;

import tool.model.MyNode;

public abstract class AbstractFormat {
    protected MyNode node;

    public void setNode(MyNode node) {
        this.node = node;
    }

    protected String encode(String str) {
        String res = str;
        res = res.replace("\"", "\\\"");
        res = res.replace("<", "&lt;");
        res = res.replace(">", "&gt;");

        return res;
    }

    public abstract String getString(int part);
}
