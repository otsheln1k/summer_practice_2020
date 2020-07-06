package summer_practice_2020.purple;

import java.util.HashSet;
import java.util.Set;

public class Group {
	private final Set<IGraph.Node> nodes = new HashSet<>();
	private final Set<IGraph.Edge> edges = new HashSet<>();
	
	public Group(IGraph g, Iterable<IGraph.Node> nodes) {
		for (IGraph.Node n : nodes) {
			this.nodes.add(n);
			for (IGraph.Edge e : g.getEdgesFrom(n)) {
				IGraph.Node m = e.firstNode().equals(n) ? e.secondNode() : e.firstNode();
				if (this.nodes.contains(m)) {
					this.edges.add(e);
				}
			}
		}
	}
	
	// copy ctor
	public Group(Group grp) {
		this.nodes.addAll(grp.nodes);
		this.edges.addAll(grp.edges);
	}
	
	public Group clone() throws CloneNotSupportedException {
		Group clone = (Group) super.clone();
		return new Group(this);
	}
	
	Iterable<IGraph.Edge> getEdges() {
		return edges;
	}

	Iterable<IGraph.Node> getNodes() {
		return nodes;
	}
}
