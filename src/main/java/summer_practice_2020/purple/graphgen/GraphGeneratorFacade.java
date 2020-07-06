package summer_practice_2020.purple.graphgen;

import java.util.Random;

import summer_practice_2020.purple.IGraph;

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

	public void generateGraph(IGraph g, int nodesCount, boolean connected) {
		GraphEdgeGenerator edgeGen = connected ? makeConnectedGenerator()
				: makeUnconnectedGenerator();
		GraphGenerator gen = new GraphGenerator(
				nodesCount, edgeGen,
				() -> rng.nextDouble() * 24 + 1,
				new AlphabetNodeNameGenerator());
		gen.generateGraph(g);
	}

	private GraphEdgeGenerator makeUnconnectedNEdgesGenerator(int edgesCount) {
		return new ShuffleGraphEdgeGenerator(edgesCount);
	}

	private GraphEdgeGenerator makeConnectedNEdgesGenerator(int nodesCount,
			int edgesCount) {
		GraphEdgeGenerator g1 = new DividerSpanningTreeEdgeGenerator();
		GraphEdgeGenerator g2 = new ShuffleGraphEdgeGenerator(
				edgesCount - nodesCount + 1);
		return g -> {
			g1.generateEdges(g);
			g2.generateEdges(g);
		};
	}

	public void generateGraphByNEdges(IGraph g,
			int nodesCount, int edgesCount, boolean connected) {
		// TODO: check that edgesCount >= nodesCount - 1 if connected
		GraphEdgeGenerator edgeGen = connected ? makeConnectedNEdgesGenerator(nodesCount, edgesCount)
				: makeUnconnectedNEdgesGenerator(edgesCount);
		GraphGenerator gen = new GraphGenerator(
				nodesCount, edgeGen,
				() -> rng.nextDouble() * 24 + 1,
				new AlphabetNodeNameGenerator());
		gen.generateGraph(g);
	}
}
