package summer_practice_2020.purple.rendering;

import javafx.scene.canvas.Canvas; 
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import summer_practice_2020.purple.Graph;
import summer_practice_2020.purple.IGraph;

import java.util.Random;
import java.util.Set;


public class Renderer {
    Canvas workingCanvas;
    GraphicsContext graphicsContext;
    Graph graph;
    Set<IGraph.Edge> edgeSet;
    Node[] nodes;
    Edge[] edges;

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
            if (node.getPosX() == -1) {
                posx = 150 + 75 * Math.cos(2 * Math.PI / 360 * angle);
                posy = 150 - 75 * Math.sin(2 * Math.PI / 360 * angle);
                angle += angleStep;
                node.setPosX(posx);
                node.setPosY(posy);
            }
            nodeList.addNode(node, node.getPosX(), node.getPosY(), Color.rgb(255, 255, 255));
        }

        for (IGraph.Edge edge : this.graph.getEdges()) {
            edgeList.addEdge(edge, nodeList.getNodeArray());
        }

        this.edges = edgeList.getEdgeArray();
        this.nodes = nodeList.getNodeArray();

        clear();

        for (int i = 0; i < edgeList.getEdgeArray().length; i++) {
            drawEdge(edges[i]);
        }

        for (int i = 0; i < nodeList.getNodeArray().length; i++) {
            this.nodes[i].updateRadius();
            drawNode(this.nodes[i]);
        }
    }


    public void drawNode(Node node) {
        this.graphicsContext.setFill(node.getColor());
        this.graphicsContext.setLineWidth(1);
        this.graphicsContext.setStroke(Color.rgb(0, 0, 0));
        this.graphicsContext.fillOval(node.getPosx() - node.getRadius(), node.getPosy() - node.getRadius(),
                node.getRadius() * 2, node.getRadius() * 2);
        this.graphicsContext.strokeOval(node.getPosx() - node.getRadius(), node.getPosy() - node.getRadius(),
                node.getRadius() * 2, node.getRadius() * 2);
        this.graphicsContext.strokeText(node.getTitle(), node.getPosx() - node.getRadius() / 6.0, node.getPosy() + 3);
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

        double middlePosX = (node2.getPosx() - node1.getPosx()) / 2;
        double middlePosY = (node2.getPosy() - node1.getPosy()) / 2;
        int approximatedValueofWeight = (int) edge.getWeight();

        this.graphicsContext.setStroke(Color.rgb(0, 0, 0));

        this.graphicsContext.strokeLine(node1.getPosx(), node1.getPosy(), node2.getPosx(), node2.getPosy());
        this.graphicsContext.setLineWidth(1);
        this.graphicsContext.setFill(Color.rgb(255, 255, 255));
        this.graphicsContext.fillRect(node1.getPosx() + middlePosX - (String.valueOf(approximatedValueofWeight).length() + 1) * 6, node1.getPosy() + middlePosY - 10,
                (String.valueOf(Math.round(edge.getWeight())).length() + 2) * 6, 15);
        this.graphicsContext.strokeRect(node1.getPosx() + middlePosX - (String.valueOf(approximatedValueofWeight).length() + 1) * 6, node1.getPosy() + middlePosY - 10,
                (String.valueOf(Math.round(edge.getWeight())).length() + 2) * 6, 15);
        this.graphicsContext.strokeText(Integer.toString(approximatedValueofWeight), node1.getPosx() + middlePosX - String.valueOf(approximatedValueofWeight).length() * 6, node1.getPosy() + middlePosY + 3);
    }

    public Edge isEdgePosition(double posx, double posy) {
        double edgePosX;
        double edgePosY;
        Node node1;
        Node node2;
        double middlePosX;
        double middlePosY;
        int approximatedValueofWeight;
        if (graph != null && this.edges != null) {
            for (Edge edge : this.edges) {
                node1 = edge.getNode1();
                node2 = edge.getNode2();
                middlePosX = (node2.getPosx() - node1.getPosx()) / 2;
                middlePosY = (node2.getPosy() - node1.getPosy()) / 2;
                approximatedValueofWeight = (int) edge.getWeight();
                edgePosX = node1.getPosx() + middlePosX -
                        (String.valueOf(approximatedValueofWeight).length() + 1) * 6;
                edgePosY = node1.getPosy() + middlePosY - 10;
                if(posx >= edgePosX && posx < edgePosX + (String.valueOf(Math.round(edge.getWeight())).length() + 2) * 6) {
                    if (posy >= edgePosY && posy < edgePosY + 15) {
                        return edge;
                    }
                }
            }
        }
        return null;
    }

    public Node isNodePosition(double posx, double posy) {
        double nodePosX;
        double nodePosy;
        if (graph != null && this.nodes != null) {
            for (Node node : this.nodes) {
                nodePosX = node.getPosx();
                nodePosy = node.getPosy();
                if (Math.pow(Math.abs(posx - nodePosX), 2) + Math.pow(Math.abs(posy - nodePosy), 2) <= Math.pow(node.getRadius(), 2)) {
                    return node;
                }
            }
        }
        return null;
    }
}
