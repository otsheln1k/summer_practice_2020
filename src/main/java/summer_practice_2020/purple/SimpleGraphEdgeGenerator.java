package summer_practice_2020.purple;

import java.util.Random;

public class SimpleGraphEdgeGenerator implements GraphEdgeGenerator {

	private final double edgeProb;
	private final Random rng = new Random();

	public SimpleGraphEdgeGenerator(double edgeProb) {
		this.edgeProb = edgeProb;
	}

	@Override
	public void generateEdges(IGraph g) {
		for (IGraph.Node i : g.getNodes()) {
			for (IGraph.Node j : g.getNodes()) {
				if (i == j || g.getEdgeBetween(i, j) != null) {
					continue;
				}

				if (rng.nextDouble() < edgeProb) {
					g.addEdge(i, j);
				}
			}
		}
	}

}
