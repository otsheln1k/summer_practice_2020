package summer_practice_2020.purple.boruvka;

import java.util.HashSet;
import java.util.Set;

import summer_practice_2020.purple.IGraph;

public class Group {
	private final Set<IGraph.Node> nodes = new HashSet<>();
	private int id;

	public Group(int id) {
		this.id = id;
	}

	// copy ctor
	public Group(Group grp) {
		this.id = grp.id;
		grp.nodes.forEach(this.nodes::add);
	}

    public Group clone() {
        return new Group(this);
    }

	public int getId() {
		return id;
	}

	public boolean hasEdge(IGraph.Edge e) {
		return nodes.contains(e.firstNode())
				&& nodes.contains(e.secondNode());
	}

    public boolean HasEdge(IGraph.Edge e) {
        return nodes.contains(e.firstNode())
                || nodes.contains(e.secondNode());
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
		this.id = g.id;
	}

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

	public Set<IGraph.Node> getNodesGroup(){
		return nodes;
	}
	
	public boolean hasNode(IGraph.Node node) {
		return nodes.contains(node);
	}
}
