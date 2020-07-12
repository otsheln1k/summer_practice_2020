package summer_practice_2020.purple.rendering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import summer_practice_2020.purple.boruvka.BoruvkaSnapshot;
import summer_practice_2020.purple.boruvka.Group;


public class Renderer {
    Canvas workingCanvas;
    GraphicsContext graphicsContext;

    Graph graph;
    BoruvkaSnapshot snapshot;
    Set<IGraph.Edge> edgeSet;

    Node[] nodes;
    Edge[] edges;

    private final List<Color> colors = new ArrayList<>();
    private final Map<IGraph.Node, Group> groupMap = new HashMap<>();

	private void buildGroupMap(Iterable<Group> groups) {
		groupMap.clear();
		for (Group grp : groups) {
			for (IGraph.Node n : grp.getNodes()) {
				groupMap.put(n, grp);
			}
		}
	}

	public Renderer(Canvas canvas) {
		this.workingCanvas = canvas;
		this.graphicsContext = this.workingCanvas.getGraphicsContext2D();
	}

	public void clear() {
		graphicsContext.clearRect(0, 0, workingCanvas.getWidth(), workingCanvas.getHeight());
	}

	public void setGraph(Graph graph) {
        this.snapshot = null;
        this.edgeSet = null;
        this.graph = graph;
    }
	
	public void setEdgeSet(Set<IGraph.Edge> edgeSet) {
		this.snapshot = null;
		this.groupMap.clear();
		this.edgeSet = edgeSet;
	}
	
	public void setSnapshot(BoruvkaSnapshot snapshot) {
		if (this.snapshot == null && this.edgeSet == null) {
			generateColors(this.graph.nodesCount());
		}
		this.snapshot = snapshot;
		if (this.snapshot != null) {
			buildGroupMap(this.snapshot.getGroups());
			this.edgeSet = null;
		} else {
			this.groupMap.clear();
			this.edgeSet = null;
		}
	}

	private static double doubleInRange(Random rng, double low, double high) {
		return low + rng.nextDouble() * (high - low);
	}

	private static Color generateColor(Random rng) {
		double hue = rng.nextDouble() * 360.0;
		double sat = doubleInRange(rng, 0.5, 1.0);
		double val = doubleInRange(rng, 0.75, 1.0);
		return Color.hsb(hue, sat, val);
	}

	private void generateColors(int n) {//
		Random rng = new Random();
		colors.clear();
		for (int i = 0; i < n; i++) {
			colors.add(generateColor(rng));
		}
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

			Color color = Color.WHITE;
			Node.HighlightType hl = Node.HighlightType.NORMAL;
			if (this.snapshot != null) {
				Group g = groupMap.get(node);
				color = colors.get(g.getId());
				if (this.snapshot.getCurrentGroup().hasNode(node)) {
					hl = Node.HighlightType.MERGED_TO;
				} else if (this.snapshot.getNextGroup().hasNode(node)) {
					hl = Node.HighlightType.MERGED;
				}
			}

			Node n = new Node(node, node.getPosX(), node.getPosY(), color, hl);
			nodeList.addNode(n);
		}

		List<Node> nodes = Arrays.asList(nodeList.getNodeArray());
		for (IGraph.Edge edge : this.graph.getEdges()) {
			Color color = Color.BLACK;
			Edge.HighlightType hl = Edge.HighlightType.NORMAL;

			if (this.snapshot != null) {
				Group g1 = groupMap.get(edge.firstNode());
				Group g2 = groupMap.get(edge.secondNode());

				if (g1 == g2) {
					color = colors.get(g1.getId());
				}

				if (edge == this.snapshot.getSelectedEdge()) {
					hl = Edge.HighlightType.LAST_SELECTED;
				} else if (this.snapshot.getEdgeAvailable(edge)) {
					hl = Edge.HighlightType.AVAILABLE;
				}
			}

			edgeList.addEdge(Edge.amongNodes(edge, nodes)
					.withColor(color)
					.withHighlightType(hl));
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
		this.graphicsContext.setStroke(Color.BLACK);
		this.graphicsContext.setFill(node.getColor());
		switch (node.getHighlightType()) {
		case NORMAL:
			this.graphicsContext.setLineWidth(1);
			break;
		case MERGED:
			this.graphicsContext.setStroke(Color.GREY);
			this.graphicsContext.setLineWidth(3);
			break;
		case MERGED_TO:
			this.graphicsContext.setLineWidth(3);
			break;
		}

		this.graphicsContext.fillOval(node.getPosx() - node.getRadius(), node.getPosy() - node.getRadius(),
				node.getRadius() * 2, node.getRadius() * 2);
		this.graphicsContext.strokeOval(node.getPosx() - node.getRadius(), node.getPosy() - node.getRadius(),
				node.getRadius() * 2, node.getRadius() * 2);

		this.graphicsContext.setLineWidth(1);
		this.graphicsContext.setStroke(Color.BLACK);
		this.graphicsContext.setTextAlign(TextAlignment.CENTER);
		this.graphicsContext.setTextBaseline(VPos.CENTER);
		this.graphicsContext.strokeText(node.getTitle(), node.getPosx(), node.getPosy());
	}

	private boolean pickedEdge(IGraph.Edge edge) {
		if (this.snapshot != null) {
			return this.snapshot.getEdgePicked(edge);
		}
		return this.edgeSet.contains(edge);
	}

	public void drawEdge(Edge edge) {
		final double xpadding = 3.0;
		final double ypadding = 3.0;

		Node node1 = edge.getNode1();
		Node node2 = edge.getNode2();
		if (this.snapshot == null && this.edgeSet == null) {
			this.graphicsContext.setLineWidth(3);
		} else if (pickedEdge(edge.getEdge())) {
			this.graphicsContext.setLineWidth(7);
		} else {
			this.graphicsContext.setLineWidth(1);
		}

		double w = edge.getWeight();

		this.graphicsContext.setStroke(edge.getColor());

		this.graphicsContext.strokeLine(node1.getPosx(), node1.getPosy(), node2.getPosx(), node2.getPosy());
		this.graphicsContext.setLineWidth(1);
		this.graphicsContext.setFill(Color.WHITE);
		this.graphicsContext.setStroke(Color.BLACK);

		String label = String.format("%.3g", w);

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

		switch (edge.getHightlightType()) {
		case NORMAL:
			break;
		case LAST_SELECTED:
			this.graphicsContext.setStroke(Color.GREEN);
			break;
		case AVAILABLE:
			this.graphicsContext.setStroke(Color.DARKORANGE);
			break;
		}

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
