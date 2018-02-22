package tool.graphviz;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Label;
import guru.nidi.graphviz.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class GraphvizUml {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphvizUml.class);

    public void createUml() {
        Node
                init = node("init"),
                execute = node("execute"),
                compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)),
                mkString = node("mkString").with(Label.of("make a\nstring")),
                printf = node("printf");

        Graph g = graph("example2").directed().with(
                node("main").with(Shape.RECTANGLE).link(
                        to(node("parse").link(execute)).with("weight", 8),
                        to(init).with(Style.DOTTED),
                        node("cleanup"),
                        to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)),
                execute.link(
                        graph().with(mkString, printf),
                        to(compare).with(Color.RED)),
                init.link(mkString));

        try {
            Graphviz.fromGraph(g).width(900).render(Format.XDOT)
            .toFile(new File("C:/Users/SBT-Vlasenko-PV/Test/ex2.dot"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
