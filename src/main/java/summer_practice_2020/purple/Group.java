package summer_practice_2020.purple;

import java.util.HashSet;
import java.util.Set;

public class Group {
	private final Set<IGraph.Node> nodes = new HashSet<>();

	public Group() {
	}

	public Group(Iterable<IGraph.Node> nodes) {
		nodes.forEach(this.nodes::add);
	}

	// copy ctor
	public Group(Group grp) {
		grp.nodes.forEach(this.nodes::add);
	}

	public Group clone() {
		return new Group(this);
	}

	public boolean hasEdge(IGraph.Edge e) {
		return nodes.contains(e.firstNode())
				&& nodes.contains(e.secondNode());
	}

	public void addNode(IGraph.Node node) {
		nodes.add(node);
	}

	public Iterable<IGraph.Node> getNodes() {
		return nodes;
	}

	public void merge(Group g) {
		g.nodes.forEach(this.nodes::add);
		g.nodes.clear();
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public Set<IGraph.Node> getNodesGroup(){
		return nodes;
	}
}
