package tool.model.ast;

import java.util.ArrayList;

public class InterfaceNode extends ModifiersNode {
    protected ArrayList<String> interfacesList;

    public InterfaceNode(String name) {
        super(name);
        this.interfacesList = new ArrayList<>();
    }

    @Override
    public String getNodeType() {
        return "interface";
    }

    public ArrayList<String> getInterfacesList() {
        return interfacesList;
    }

    public void addInterface(String interfaceName) {
        this.interfacesList.add(interfaceName);
    }

}
