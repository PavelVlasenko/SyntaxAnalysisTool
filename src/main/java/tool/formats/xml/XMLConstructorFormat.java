package tool.formats.xml;

import tool.model.ast.ConstructorNode;

public class XMLConstructorFormat extends AbstractFormat {
    @Override
    public String getString(int part) {
        ConstructorNode n = (ConstructorNode) node;
        if(part == 1) {
            return "</" + n.getNodeType() + ">\n";
        } else {
            return "<" + n.getNodeType() + " name=\"" + n.getName()
                    + "\" filePath=\"" + n.getFilePath()
                    + "\" lineNumber=\"" + n.getLineNumber()
                    + "\" visibility=\"" + n.getVisibility()
                    + "\">\n<nblocalvar>" + n.getNbLocalVar()
                    + "</nblocalvar>\n<nbif>" + n.getNbIf()
                    + "</nbif>\n<nbswitch>" + n.getNbSwitch()
                    + "</nbswitch>\n<nbfor>" + n.getNbFor()
                    + "</nbfor>\n<nbwhile>" + n.getNbWhile()
                    + "</nbwhile>\n<nbbreak>" + n.getNbBreak()
                    + "</nbbreak>\n<nbcontinue>" + n.getNbContinue()
                    + "</nbcontinue>\n";
        }
    }
}
