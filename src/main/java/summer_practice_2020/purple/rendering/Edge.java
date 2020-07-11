package summer_practice_2020.purple.rendering;

import javafx.scene.paint.Color;
import summer_practice_2020.purple.IGraph;

public class Edge {
    private final IGraph.Edge edge;
    private final Node node1;
    private final Node node2;
    private Color color;
    private HighlightType hlType;

    public enum HighlightType {
    	NORMAL,
    	LAST_SELECTED,
    	AVAILABLE,
    }

    public HighlightType getHightlightType() {
    	return hlType;
    }

    public Color getColor() {
		return color;
	}

	public Edge withColor(Color color) {
		this.color = color;
		return this;
	}

	public Edge withHighlightType(HighlightType hlType) {
		this.hlType = hlType;
		return this;
	}

	private Edge(IGraph.Edge edge, Node node1, Node node2) {
		this.edge = edge;
        this.node1 = node1;
        this.node2 = node2;
    }

	public static Edge amongNodes(IGraph.Edge edge, Iterable<Node> nodes) {
        Node node1 = null;
        Node node2 = null;
        for (Node n : nodes) {
            if (edge.firstNode().equals(n.getNode())) {
                node1 = n;
            } else if (edge.secondNode().equals(n.getNode())) {
                node2 = n;
            }
        }

        return new Edge(edge, node1, node2);
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
