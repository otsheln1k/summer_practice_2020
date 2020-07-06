package summer_practice_2020.purple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoruvkaSnapshot {
	private final List<Group> groups = new ArrayList<>();
	private final BoruvkaStep step;
	private final Set<IGraph.Edge> pickedEdges = new HashSet<>();
	
	public BoruvkaSnapshot(Iterable<Group> groups,
			BoruvkaStep step,
			Iterable<IGraph.Edge> edges) {
		groups.forEach(this.groups::add);
		edges.forEach(this.pickedEdges::add);
		this.step = step;
	}
	
	public Iterable<Group> getGroups() {
		return groups;
	}
	
	public BoruvkaStep getStep() {
		return step;
	}
	
	public boolean getEdgePicked(IGraph.Edge e) {
		return pickedEdges.contains(e);
	}
}
