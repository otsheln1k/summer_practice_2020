package summer_practice_2020.purple.graphgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import summer_practice_2020.purple.IGraph;

public class GraphGeneratorFacade {
	private final Random rng = new Random();

	private GraphEdgeWeightGenerator makeDefaultWeightGenerator() {
		return () -> rng.nextDouble() * 24 + 1;
	}

	private GraphNodeNameGenerator makeDefaultNameGenerator() {
		return new AlphabetNodeNameGenerator();
	}

	private GraphEdgeGenerator makeUnconnectedGenerator() {
		return new SimpleGraphEdgeGenerator(0.3);
	}

	private GraphEdgeGenerator makeConnectedGenerator() {
		GraphEdgeGenerator g1 = new DividerSpanningTreeEdgeGenerator();
		GraphEdgeGenerator g2 = new SimpleGraphEdgeGenerator(0.2);
		return (g, ns) -> {
			g1.generateEdgesOnNodes(g, ns);
			g2.generateEdgesOnNodes(g, ns);
		};
	}

	public void generateGraph(IGraph g, int nodesCount, boolean connected) {
		GraphEdgeGenerator edgeGen = connected ? makeConnectedGenerator()
				: makeUnconnectedGenerator();
		GraphGenerator gen = new GraphGenerator(
				nodesCount, edgeGen,
				makeDefaultWeightGenerator(),
				makeDefaultNameGenerator());
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
		return (g, ns) -> {
			g1.generateEdgesOnNodes(g, ns);
			g2.generateEdgesOnNodes(g, ns);
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

	public void randomlyDistribute(List<Integer> ints, int total) {
		int n = ints.size();
		for (int i = 0; i < total; i++) {
			int idx = rng.nextInt(n);
			ints.set(idx, ints.get(idx) + 1);
		}
	}

	public void generateComponents(IGraph g, int nodesCount, int compsCount) {
		GraphEdgeGenerator edgeGen = makeConnectedGenerator();
		GraphGenerator gen = new GraphGenerator(
				nodesCount, edgeGen,
				makeDefaultWeightGenerator(),
				makeDefaultNameGenerator());

		List<Integer> counts = new ArrayList<>();
		for (int i = 0; i < compsCount; i++) {
			counts.add(0);
		}
		randomlyDistribute(counts, nodesCount);

		gen.generateGraphComponents(g, counts);
	}
}
