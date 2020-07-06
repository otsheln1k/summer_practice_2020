package summer_practice_2020.purple.graphgen;

import summer_practice_2020.purple.IGraph;

// NOTE: nodes are pre-generated
@FunctionalInterface
public interface GraphEdgeGenerator {
	public void generateEdges(IGraph g);
}
