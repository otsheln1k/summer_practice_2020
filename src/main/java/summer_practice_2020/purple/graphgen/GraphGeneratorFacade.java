package summer_practice_2020.purple.graphgen;

import summer_practice_2020.purple.IGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private void generateGraphWithEdgeGen(IGraph g,
			GraphEdgeGenerator edgeGen, int nodesCount) {
		GraphGenerator gen = new GraphGenerator(
				nodesCount, edgeGen,
				makeDefaultWeightGenerator(),
				makeDefaultNameGenerator());
		gen.generateGraph(g);
	}

	public void generateGraph(IGraph g, int nodesCount, boolean connected) {
		GraphEdgeGenerator edgeGen = connected ? makeConnectedGenerator()
				: makeUnconnectedGenerator();
		generateGraphWithEdgeGen(g, edgeGen, nodesCount);
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
		generateGraphWithEdgeGen(g, edgeGen, nodesCount);
	}

	public void randomlyDistribute(List<Integer> ints, int total) {
		int n = ints.size();
		for (int i = 0; i < total; i++) {
			int idx = rng.nextInt(n);
			ints.set(idx, ints.get(idx) + 1);
		}
	}

	public void randomlyDistributeWithUpperBound(List<Integer> ints,
			int total, List<Integer> bounds) {
		List<Integer> availIndexes = new ArrayList<>();
		for (int i = 0; i < ints.size(); i++) {
			if (ints.get(i) < bounds.get(i)) {
				availIndexes.add(i);
			}
		}
		for (int i = 0; i < total; i++) {
			int idx = rng.nextInt(availIndexes.size());
			int realIdx = availIndexes.get(idx);
			Integer newVal = ints.get(realIdx) + 1;
			ints.set(realIdx, newVal);
			if (newVal == bounds.get(realIdx)) {
				availIndexes.remove(idx);
			}
		}
	}

	private List<Integer> distributeList(int n, int total) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			res.add(1);
		}
		randomlyDistribute(res, total - n);
		return res;
	}

	public void generateComponents(IGraph g, int nodesCount, int compsCount) {
		GraphEdgeGenerator edgeGen = makeConnectedGenerator();

		GraphGenerator gen = new GraphGenerator(
				nodesCount, edgeGen,
				makeDefaultWeightGenerator(),
				makeDefaultNameGenerator());

		List<Integer> counts = distributeList(compsCount, nodesCount);

		gen.generateGraphComponents(g, counts);
	}

	private List<GraphEdgeGenerator> distributeEdgeGenerators(
			List<Integer> nodeCounts, int edgesCount, int compsCount) {
		List<Integer> genCounts = new ArrayList<>();
		int total = edgesCount;

		for (int nc : nodeCounts) {
			genCounts.add(nc - 1);
			total -= nc - 1;
		}

		List<Integer> maxEdgeCounts = new ArrayList<>();
		nodeCounts.forEach(nc -> maxEdgeCounts.add(nc * (nc-1) / 2));

		randomlyDistributeWithUpperBound(genCounts, total, maxEdgeCounts);

		List<GraphEdgeGenerator> gens = new ArrayList<>();
		for (int i = 0; i < compsCount; i++) {
			gens.add(makeConnectedNEdgesGenerator(
					nodeCounts.get(i), genCounts.get(i)));
		}

		return gens;
	}

	public void generateComponentsWithNEdges(IGraph g,
			int nodesCount, int edgesCount, int compsCount) {

		GraphGenerator gen = new GraphGenerator(
				nodesCount, null,
				makeDefaultWeightGenerator(),
				makeDefaultNameGenerator());

		List<Integer> counts = distributeList(compsCount, nodesCount);

		List<GraphEdgeGenerator> gens =
				distributeEdgeGenerators(counts, edgesCount, compsCount);

		gen.generateGraphComponents(g, counts, gens);
	}
}
