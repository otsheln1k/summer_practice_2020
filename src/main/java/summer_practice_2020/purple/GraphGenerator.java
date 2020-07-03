package summer_practice_2020.purple;

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

	private void genNodes(Graph g) {
		for (int i = 0; i < nodesCount; i++) {
			Graph.Node n = g.addNode();
			n.setTitle(nameGen.generateName());
		}
	}

	private void genWeights(Graph g) {
		for (Graph.Edge e : g.getEdges()) {
			e.setWeight(weightGen.generateWeight());
		}
	}

	public void generateGraph(Graph g) {
		genNodes(g);
		edgeGen.generateEdges(g);
		genWeights(g);
	}
}
