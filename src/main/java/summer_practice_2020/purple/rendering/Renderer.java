package summer_practice_2020.purple.rendering;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import summer_practice_2020.purple.Graph;
import summer_practice_2020.purple.IGraph;


public class Renderer {
    Canvas workingCanvas;
    GraphicsContext graphicsContext;
    Graph graph;
    Set<IGraph.Edge> edgeSet;
    Node[] nodes;
    Edge[] edges;

    public Renderer(Canvas canvas) {
        this.workingCanvas = canvas;
        this.graphicsContext = this.workingCanvas.getGraphicsContext2D();
    }

    public void clear() {
        graphicsContext.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
    }

    public void setGraph(Graph graph) {
        this.setEdgeSet(new HashSet<>());
        this.graph = graph;
    }

    public void setEdgeSet(Set<IGraph.Edge> edgeSet) {
        this.edgeSet = edgeSet;
    }

    public void addToEdgeSet(IGraph.Edge edge) {
        if (edge == null) {
            System.out.println("edge null");
            System.exit(-1);
        } else if (this.edgeSet == null) {
            System.out.println("Edgeset null");
            System.exit(-2);
        }
        this.edgeSet.add(edge);
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
                double tmpMin = Math.min(this.workingCanvas.getWidth(), this.workingCanvas.getHeight());
                tmpMin *= 0.45;
                posx = this.workingCanvas.getWidth() / 2 + tmpMin * Math.cos(2 * Math.PI / 360 * angle);
                posy = this.workingCanvas.getHeight() / 2 - tmpMin * Math.sin(2 * Math.PI / 360 * angle);
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
        this.graphicsContext.setTextAlign(TextAlignment.CENTER);
        this.graphicsContext.setTextBaseline(VPos.CENTER);
        this.graphicsContext.strokeText(node.getTitle(), node.getPosx(), node.getPosy());
    }

    public void drawEdge(Edge edge) {
    	final double EPS = 1e-3;
    	final double xpadding = 3.0;
    	final double ypadding = 3.0;
    	
        Node node1 = edge.getNode1();
        Node node2 = edge.getNode2();
        if (this.edgeSet != null && this.edgeSet.contains(edge.getEdge())) {
            this.graphicsContext.setLineWidth(7);
        } else if (this.edgeSet == null) {
            this.graphicsContext.setLineWidth(3);
        } else {
            this.graphicsContext.setLineWidth(1);
        }

        double w = edge.getWeight();

        this.graphicsContext.setStroke(Color.rgb(0, 0, 0));

        this.graphicsContext.strokeLine(node1.getPosx(), node1.getPosy(), node2.getPosx(), node2.getPosy());
        this.graphicsContext.setLineWidth(1);
        this.graphicsContext.setFill(Color.rgb(255, 255, 255));
        
        String label = String.format("%.6g", w);
        
        Text t = new Text();
        t.setFont(this.graphicsContext.getFont());
        t.setText(label);
        Bounds b = t.getLayoutBounds();
        
        double width = b.getWidth() + xpadding*2;
        double height = b.getHeight() + ypadding*2;
        
        double hwidth = width / 2;
        double hheight = height / 2;
        
        double cx = (node2.getPosx() + node1.getPosx()) / 2;
        double cy = (node2.getPosy() + node1.getPosy()) / 2;
        
        double left = cx - hwidth;
        double top = cy - hheight;
        
        this.graphicsContext.fillRect(left, top, width, height);
        this.graphicsContext.strokeRect(left, top, width, height);
        
        this.graphicsContext.setTextAlign(TextAlignment.CENTER);
        this.graphicsContext.setTextBaseline(VPos.CENTER);
        this.graphicsContext.strokeText(label, cx, cy);
        this.graphicsContext.setTextAlign(TextAlignment.LEFT);
        this.graphicsContext.setTextBaseline(VPos.BASELINE);
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
                if (posx >= edgePosX && posx < edgePosX + (String.valueOf(Math.round(edge.getWeight())).length() + 2) * 6) {
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
