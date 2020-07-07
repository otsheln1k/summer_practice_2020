package summer_practice_2020.purple.rendering;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import summer_practice_2020.purple.Graph;
import summer_practice_2020.purple.IGraph;
import javafx.scene.shape.ArcType;

import java.util.Random;
import java.util.Set;
import java.util.Timer;


public class Renderer {
    Canvas workingCanvas;
    GraphicsContext graphicsContext;
    Graph graph;
    Set<IGraph.Edge> edgeSet;

    public Renderer(Canvas canvas) {
        this.workingCanvas = canvas;
        workingCanvas.setWidth(1000);
        workingCanvas.setHeight(1000);
        this.graphicsContext = this.workingCanvas.getGraphicsContext2D();
    }

    public void clear() {
        graphicsContext.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
    }

    public void setGraph(Graph graph) {
        this.setEdgeSet(null);
        this.graph = graph;
    }

    public void setEdgeSet(Set<IGraph.Edge> edgeSet) {
        this.edgeSet = edgeSet;
    }

    public void drawGraph() {
        NodeList nodeList = new NodeList(graph.nodesCount());
        EdgeList edgeList = new EdgeList(graph.edgesCount());
        double angle = 90.0;
        double angleStep = 360.0 / graph.nodesCount();
        double posx;
        double posy;
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());

        for (IGraph.Node node : this.graph.getNodes()) {
            posx = 150 + 75 * Math.cos(2 * Math.PI / 360 * angle);
            posy = 150 - 75 * Math.sin(2 * Math.PI / 360 * angle);
            angle += angleStep;
            nodeList.addNode(node, posx, posy, Color.rgb((int) (r.nextDouble() * 155) + 100, 0, (int) (r.nextDouble() * 155) + 100));
        }

        for (IGraph.Edge edge : this.graph.getEdges()) {
            edgeList.addEdge(edge, nodeList.getNodeArray());
        }

        Edge[] edges = edgeList.getEdgeArray();
        Node[] nodes = nodeList.getNodeArray();

        for (int i = 0; i < edgeList.getEdgeArray().length; i++) {
            drawEdge(edges[i]);
        }

        for (int i = 0; i < nodeList.getNodeArray().length; i++) {
            drawNode(nodes[i]);
        }
    }


    public void drawNode(Node node) {
        this.graphicsContext.setFill(node.getColor());
        this.graphicsContext.setLineWidth(1);
        this.graphicsContext.setStroke(Color.rgb(0, 0, 0));
        this.graphicsContext.fillOval(node.getPosx(), node.getPosy(), (node.getTitle().length() + 2) * 12, (node.getTitle().length() + 2) * 12);
        this.graphicsContext.strokeText(node.getTitle(), node.getPosx() + 12, node.getPosy() + ((float) (node.getTitle().length() + 2) * 8));
    }

    public void drawEdge(Edge edge) {
        Node node1 = edge.getNode1();
        Node node2 = edge.getNode2();
        if (this.edgeSet != null && this.edgeSet.contains(edge.getEdge())) {
            this.graphicsContext.setLineWidth(7);
        } else if (this.edgeSet == null) {
            this.graphicsContext.setLineWidth(3);
        } else {
            this.graphicsContext.setLineWidth(1);
        }

        this.graphicsContext.setStroke(node1.getColor());
        this.graphicsContext.strokeLine(node1.getPosx() + (node1.getTitle().

                length() + 2) * 6, node1.getPosy() + (node1.getTitle().

                length() + 2) * 6, node2.getPosx() + (node2.getTitle().

                length() + 2) * 6, node2.getPosy() + (node2.getTitle().

                length() + 2) * 6);
    }

    public void testFunc() {
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(5);
        graphicsContext.strokeLine(40, 10, 10, 40);
        graphicsContext.fillOval(10, 60, 30, 30);
        graphicsContext.strokeOval(60, 60, 30, 30);
        graphicsContext.fillRoundRect(110, 60, 30, 30, 10, 10);
        graphicsContext.strokeRoundRect(160, 60, 30, 30, 10, 10);
        graphicsContext.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        graphicsContext.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        graphicsContext.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        graphicsContext.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        graphicsContext.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        graphicsContext.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        graphicsContext.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        graphicsContext.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        graphicsContext.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }
}
