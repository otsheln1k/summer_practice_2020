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
				IGraph.Node m = e.otherNode(n);
				if (this.nodes.contains(m)) {
					this.edges.add(e);
				}
			}
		}
	}
	
	// copy ctor
	public Group(Group grp) {
		grp.nodes.forEach(this.nodes::add);
		grp.edges.forEach(this.edges::add);
	}
	
	public Group clone() {
		return new Group(this);
	}
	
	Iterable<IGraph.Edge> getEdges() {
		return edges;
	}

	Iterable<IGraph.Node> getNodes() {
		return nodes;
	}
}
