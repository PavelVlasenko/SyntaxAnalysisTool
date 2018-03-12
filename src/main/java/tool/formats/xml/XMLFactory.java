package tool.formats.xml;

import tool.model.MyNode;

public class XMLFactory extends FormatFactory {
    @Override
    public AbstractFormat getFormat(MyNode node) {
        AbstractFormat f;
        switch(node.getNodeType()) {
            case "class":
                f = new XMLClassFormat();
                break;
            case "constructor":
                f = new XMLConstructorFormat();
                break;
            case "field":
                f = new XMLFieldFormat();
                break;
            case "interface":
                f = new XMLInterfaceFormat();
                break;
            case "method":
                f = new XMLMethodFormat();
                break;
            case "parameter":
                f = new XMLParameterFormat();
                break;
            case "root":
                f = new XMLRootFormat();
                break;
            default:
                f = new XMLUnkownFormat();
                break;
        }
        f.setNode(node);
        return f;
    }
}
