package tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.core.AstGenerator;
import tool.graphviz.GraphvizUml;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String ... args) {
        LOGGER.info("Start program.");
//        AstGenerator astGenerator = new AstGenerator();
//        astGenerator.generateAst();

        GraphvizUml graphvizUml = new GraphvizUml();
        graphvizUml.createUml();
    }
}
