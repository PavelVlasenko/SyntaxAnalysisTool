package tool.formats.xml;

import tool.ast.model.InterfaceNode;
import tool.formats.AbstractFormat;

public class XMLInterfaceFormat extends AbstractFormat {
    @Override
    public String getString(int part) {
        InterfaceNode n = (InterfaceNode) node;
        if(part == 1) {
            return "</" + n.getNodeType() + ">\n";
        } else {
            String res = "<" + n.getNodeType() + " name=\"" + n.getName()
                    + "\" filePath=\"" + n.getFilePath()
                    + "\" lineNumber=\"" + n.getLineNumber()
                    + "\" static=\"" + n.isStatic()
                    + "\" visibility=\"" + n.getVisibility()
                    + "\">\n";
            for(String interfaceName : n.getInterfacesList()) {
                res += "<extends>" + interfaceName + "</extends>\n";
            }

            return res;
        }
    }
}
