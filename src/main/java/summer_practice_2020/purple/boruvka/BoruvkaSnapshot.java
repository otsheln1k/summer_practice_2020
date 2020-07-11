package summer_practice_2020.purple.boruvka;

import summer_practice_2020.purple.IGraph;
import summer_practice_2020.purple.rendering.Edge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoruvkaSnapshot implements Cloneable{
	private final List<Group> groups;
	private final Set<IGraph.Edge> pickedEdges;
	private final Group currentGroup;
	private final Group nextGroup;
	private final Set<IGraph.Edge> availEdges;
	private final Set<IGraph.Edge> ostovEdges;
	private final IGraph.Edge selectedEdge;

	public Group getCurrentGroup() {
		return currentGroup;
	}

	public Group getNextGroup() {
		return nextGroup;
	}

	public IGraph.Edge getSelectedEdge() {
		return selectedEdge;
	}

	public Iterable<Group> getGroups() {
		return groups;
	}

	public boolean getEdgePicked(IGraph.Edge e) {
		return pickedEdges.contains(e);
	}

	public Set<IGraph.Edge> getEdgesPicked() { return ostovEdges; }

	public boolean getEdgeAvailable(IGraph.Edge e) {
		return availEdges.contains(e);
	}

	public BoruvkaSnapshot(Iterable<Group> groups,
						   Iterable<IGraph.Edge> edges,
						   Set<IGraph.Edge> nowedges,
						   Group currentGroup,
						   Group nextGroup,
						   Iterable<IGraph.Edge> availEdges,
						   IGraph.Edge selectedEdge) {

		List<Group> groupsCopy = new ArrayList<>();
		Group currentGroupCopy = currentGroup.clone();
		Group nextGroupCopy = nextGroup.clone();
		Set<IGraph.Edge> edgesCopy = new HashSet<>();
		Set<IGraph.Edge> availEdgesCopy = new HashSet<>();

		for (Group g : groups) {
			Group gc = g.clone();
			groupsCopy.add(gc);
		}

		edges.forEach(edgesCopy::add);
		availEdges.forEach(availEdgesCopy::add);

		this.groups = groupsCopy;
		this.pickedEdges = edgesCopy;
		this.currentGroup = currentGroupCopy;
		this.availEdges = availEdgesCopy;
		this.selectedEdge = selectedEdge;
		this.nextGroup = nextGroupCopy;
		this.ostovEdges = nowedges;
	}
}