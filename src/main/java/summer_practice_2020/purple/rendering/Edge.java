package summer_practice_2020.purple.rendering;

import javafx.scene.paint.Color;
import summer_practice_2020.purple.IGraph;

public class Edge {
    private final IGraph.Edge edge;
    private final Node node1;
    private final Node node2;
    private Color color;

    public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Edge(IGraph.Edge edge, Node node1, Node node2, Color color) {
        this.edge = edge;
        this.node1 = node1;
        this.node2 = node2;
        this.color = color;
    }

    public IGraph.Edge getEdge(){
        return this.edge;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public double getWeight() {
        return this.edge.getWeight();
    }
}
