package summer_practice_2020.purple;

import java.util.Random;

public class GraphGeneratorFacade {
	private final Random rng = new Random();

	private GraphEdgeGenerator makeUnconnectedGenerator() {
		return new SimpleGraphEdgeGenerator(0.6);
	}

	private GraphEdgeGenerator makeConnectedGenerator() {
		GraphEdgeGenerator g1 = new DividerSpanningTreeEdgeGenerator();
		GraphEdgeGenerator g2 = new SimpleGraphEdgeGenerator(0.5);
		return g -> {
			g1.generateEdges(g);
			g2.generateEdges(g);
		};
	}

	public void generateGraph(Graph g, int nodesCount, boolean connected) {
		GraphEdgeGenerator edgeGen = connected ? makeConnectedGenerator()
				: makeUnconnectedGenerator();
		GraphGenerator gen = new GraphGenerator(
				nodesCount, edgeGen,
				() -> rng.nextDouble() * 24 + 1,
				new AlphabetNodeNameGenerator());
		gen.generateGraph(g);
	}
}
