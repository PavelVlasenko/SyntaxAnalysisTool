package tool.formats.xml;

import tool.model.ast.ParameterNode;
import tool.formats.AbstractFormat;

public class XMLParameterFormat extends AbstractFormat {
    @Override
    public String getString(int part) {
        ParameterNode n = (ParameterNode) node;
        if(part == 1) {
            return "";
        } else {
            return "<" + n.getNodeType() + " name=\"" + n.getName()
                    + "\" filePath=\"" + n.getFilePath()
                    + "\" lineNumber=\"" + n.getLineNumber()
                    + "\" type=\"" + n.getType()
                    + "\"/>";
        }
    }
}
