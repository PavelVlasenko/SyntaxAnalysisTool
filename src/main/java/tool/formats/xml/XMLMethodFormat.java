package tool.formats.xml;

import tool.ast.model.MethodNode;
import tool.formats.AbstractFormat;

public class XMLMethodFormat extends AbstractFormat {
    @Override
    public String getString(int part) {
        MethodNode n = (MethodNode) node;
        if(part == 1) {
            return "</" + n.getNodeType() + ">\n";
        } else {
            String res = "<" + n.getNodeType() + " name=\"" + n.getName()
                    + "\" filePath=\"" + n.getFilePath()
                    + "\" lineNumber=\"" + n.getLineNumber()
                    + "\" visibility=\"" + n.getVisibility()
                    + "\" static=\"" + n.isStatic()
                    + "\" abstract=\"" + n.isAbstract()
                    + "\" returnType=\"" + n.getReturnType()
                    + "\">";
            if(!n.isAbstract()) {
                res += "\n<nblocalvar>" + n.getNbLocalVar()
                        + "</nblocalvar>\n<nbif>" + n.getNbIf()
                        + "</nbif>\n<nbswitch>" + n.getNbSwitch()
                        + "</nbswitch>\n<nbfor>" + n.getNbFor()
                        + "</nbfor>\n<nbwhile>" + n.getNbWhile()
                        + "</nbwhile>\n<nbbreak>" + n.getNbBreak()
                        + "</nbbreak>\n<nbcontinue>" + n.getNbContinue()
                        + "</nbcontinue>\n";
            }
            return res;
        }
    }
}
