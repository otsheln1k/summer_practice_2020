package summer_practice_2020.purple.rendering;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import summer_practice_2020.purple.Graph;
import summer_practice_2020.purple.IGraph;

public class Renderer {
    Canvas workingCanvas;
    GraphicsContext graphicsContext;
    Graph graph;

    double nextposx = 10;
    double nextposy = 10;

    public Renderer(Canvas canvas) {
        this.workingCanvas = canvas;
        this.graphicsContext = this.workingCanvas.getGraphicsContext2D();
    }

    public void clear() {
        graphicsContext.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void drawGraph() {
        NodeList nodeList = new NodeList(graph.nodesCount());
        EdgeList edgeList = new EdgeList(graph.edgesCount());

        for (IGraph.Node node : this.graph.getNodes()) {
            this.nextposx += (node.getTitle().length() + 2) * 12;
            if (this.nextposx > this.workingCanvas.getWidth()) {
                this.nextposx = 10;
                this.nextposy += 50;
            }
            nodeList.addNode(node, this.nextposx, this.nextposy);
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
        this.graphicsContext.fillOval(node.getPosx(), node.getPosy(), (node.getTitle().length() + 2) * 12, (node.getTitle().length() + 2) * 12);
        this.graphicsContext.fillText(node.getTitle(), node.getPosx() + 12, node.getPosy() / 2 + 6);
    }

    public void drawEdge(Edge edge) {
        Node node1 = edge.getNode1();
        Node node2 = edge.getNode2();
        this.graphicsContext.moveTo(node1.getPosx() + (node1.getTitle().length() + 2) * 6, node1.getPosy() + (node1.getTitle().length() + 2) * 6);
        this.graphicsContext.lineTo(node2.getPosx() + (node2.getTitle().length() + 2) * 6, node2.getPosy() + (node2.getTitle().length() + 2) * 6);
    }
}
