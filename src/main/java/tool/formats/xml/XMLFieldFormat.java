package tool.formats.xml;

import tool.model.ast.FieldNode;
import tool.formats.AbstractFormat;

public class XMLFieldFormat extends AbstractFormat {
    @Override
    public String getString(int part) {
        FieldNode n = (FieldNode) node;
        if(part == 1) {
            return "</" + n.getNodeType() + ">\n";
        } else {
            return "<" + n.getNodeType() + " name=\"" + n.getName()
                    + "\" filePath=\"" + n.getFilePath()
                    + "\" lineNumber=\"" + n.getLineNumber()
                    + "\" type=\"" + n.getType()
                    + "\" static=\"" + n.isStatic()
                    + "\" abstract=\"" + n.isAbstract()
                    + "\" visibility=\"" + n.getVisibility()
                    + "\">\n";
        }
    }
}
