package tool.formats.xml;

public class XMLRootFormat extends AbstractFormat {
    @Override
    public String getString(int part) {
        if(part == 0)
            return "<ast>\n";
        else
            return "</ast>";
    }
}
