package tool.formats.xml;

import tool.ast.model.ClassNode;
import tool.formats.AbstractFormat;

public class XMLClassFormat extends AbstractFormat {
    @Override
    public String getString(int part) {
        ClassNode n = (ClassNode) node;
        if(part == 1) {
            return "</" + n.getNodeType() + ">\n";
        } else {
            String res = "<" + n.getNodeType() + " name=\"" + n.getName()
                    + "\" filePath=\"" + n.getFilePath()
                    + "\" lineNumber=\"" + n.getLineNumber()
                    + "\" static=\"" + n.isStatic()
                    + "\" abstract=\"" + n.isAbstract()
                    + "\" visibility=\"" + n.getVisibility()
                    + "\">\n";
            if(n.getExtension() != null) {
                res += "<extends>" + n.getExtension() + "</extends>\n";
            }
            for(String interfaceName : n.getInterfacesList()) {
                res += "<implements>" + interfaceName + "</implements>\n";
            }

            return res;
        }
    }
}
