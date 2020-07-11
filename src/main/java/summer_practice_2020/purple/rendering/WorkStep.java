package summer_practice_2020.purple.rendering;

import java.util.function.Predicate;

import summer_practice_2020.purple.IGraph;
import summer_practice_2020.purple.boruvka.BoruvkaSnapshot;
import summer_practice_2020.purple.boruvka.Group;

public class WorkStep {
	private final BoruvkaSnapshot snapshot;
	private final String description;

	public WorkStep(BoruvkaSnapshot boruvkaSnapshot) {
		this.snapshot = boruvkaSnapshot;

		IGraph.Edge edge = boruvkaSnapshot.getSelectedEdge();
		this.description = "Выбрано ребро между " + edge.firstNode().getTitle()
				+ " и " + edge.secondNode().getTitle();
	}

	public IGraph.Edge getEdge() {
		return snapshot.getSelectedEdge();
	}
	
	public Predicate<IGraph.Edge> getAvailableEdgePredicate() {
		return snapshot::getEdgeAvailable;
	}

	public Iterable<Group> getGroups() {
		return snapshot.getGroups();
	}

	public String getDescription() {
		return this.description;
	}
}
