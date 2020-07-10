package summer_practice_2020.purple;

import java.util.*;

public class BoruvkaSnapshot {
	private final List<Group> groups = new ArrayList<>();
	//private final BoruvkaStep step;
	private final Set<IGraph.Edge> pickedEdges = new HashSet<>();
	
	public BoruvkaSnapshot(Iterable<Group> groups,
			//BoruvkaStep step,
			Iterable<IGraph.Edge> edges) {
		groups.forEach(this.groups::add);
		edges.forEach(this.pickedEdges::add);
		//this.step = step;
	}
	
	public Iterable<Group> getGroups() {
		return groups;
	}
	
	//public BoruvkaStep getStep() {
	//	return step;
	//}
	
	public boolean getEdgePicked(IGraph.Edge e) {
		return pickedEdges.contains(e);
	}

	public static BoruvkaSnapshot fromMapAndSet(
			Map<IGraph.Node, Integer> compMap,
			Set<IGraph.Edge> edges) {
		Map<Integer, Group> groupMap = new HashMap<>();

		for (Map.Entry<IGraph.Node, Integer> e
				: compMap.entrySet()) {
			Group grp = groupMap.compute(e.getValue(),
					(x, pgrp) -> (pgrp == null) ? new Group() : pgrp);
			grp.addNode(e.getKey());
		}

		return new BoruvkaSnapshot(
				groupMap.values(),
				//new BoruvkaFinalStep(),
				edges);
	}

	/*public static BoruvkaSnapshot fromCopies(Iterable<Group> groups,
											 Iterable<IGraph.Edge> edges,
											 BoruvkaStep step) {
		List<Group> groupsCopy = new ArrayList<>();
		groups.forEach(g -> groupsCopy.add(g.clone()));

		Set<IGraph.Edge> edgesCopy = new HashSet<>();
		edges.forEach(edgesCopy::add);

		return new BoruvkaSnapshot(groupsCopy, step, edgesCopy);
	}*/
}
