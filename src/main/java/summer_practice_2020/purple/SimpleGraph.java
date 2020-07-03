package summer_practice_2020.purple;

import java.util.HashSet;
import java.util.Set;

import summer_practice_2020.purple.util.FilteredIterator;

public class SimpleGraph implements Graph {
	
	private static class SimpleNode implements Graph.Node {
		private String title = "";

		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public void setTitle(String title) {
			this.title = title;
		}
		
	}
	
	private static class SimpleEdge implements Graph.Edge {
		private double weight = 0.0;
		private final Node a;
		private final Node b;

		public SimpleEdge(Node a, Node b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public void setWeight(double w) {
			weight = w;
		}

		@Override
		public double getWeight() {
			return weight;
		}

		@Override
		public Node firstNode() {
			return a;
		}

		@Override
		public Node secondNode() {
			return b;
		}
		
	}
	
	private final Set<Node> nodes = new HashSet<>();
	private final Set<Edge> edges = new HashSet<>();

	@Override
	public Node addNode() {
		Node n = new SimpleNode();
		nodes.add(n);
		return n;
	}

	@Override
	public void removeNode(Node node) {
		nodes.remove(node);
	}

	@Override
	public Iterable<Node> getNodes() {
		return nodes;
	}

	@Override
	public int nodesCount() {
		return nodes.size();
	}

	@Override
	public Edge addEdge(Node a, Node b) {
		Edge e = new SimpleEdge(a, b);
		edges.add(e);
		return e;
	}

	@Override
	public void removeEdge(Edge edge) {
		edges.remove(edge);
	}

	@Override
	public Edge getEdgeBetween(Node a, Node b) {
		return edges.stream()
				.filter(e -> e.firstNode() == a && e.secondNode() == b)
				.findFirst().orElse(null);
	}
	
	@Override
	public Iterable<Edge> getEdgesFrom(Node node) {
		return () -> new FilteredIterator<>(edges.iterator(),
				e -> e.firstNode() == node || e.secondNode() == node);
	}

	@Override
	public Iterable<Edge> getEdges() {
		return edges;
	}

	@Override
	public int edgesCount() {
		return edges.size();
	}

}
