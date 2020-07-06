package summer_practice_2020.purple.graphgen;

import summer_practice_2020.purple.IGraph;

public class GraphGenerator {
	private final int nodesCount;
	private final GraphEdgeGenerator edgeGen;
	private final GraphEdgeWeightGenerator weightGen;
	private final GraphNodeNameGenerator nameGen;

	public GraphGenerator(int nodesCount,
			GraphEdgeGenerator edgeGen,
			GraphEdgeWeightGenerator weightGen,
			GraphNodeNameGenerator nameGen) {
		this.nodesCount = nodesCount;
		this.edgeGen = edgeGen;
		this.weightGen = weightGen;
		this.nameGen = nameGen;
	}

	private void genNodes(IGraph g) {
		for (int i = 0; i < nodesCount; i++) {
			IGraph.Node n = g.addNode();
			n.setTitle(nameGen.generateName());
		}
	}

	private void genWeights(IGraph g) {
		for (IGraph.Edge e : g.getEdges()) {
			e.setWeight(weightGen.generateWeight());
		}
	}

	public void generateGraph(IGraph g) {
		genNodes(g);
		edgeGen.generateEdges(g);
		genWeights(g);
	}
}
