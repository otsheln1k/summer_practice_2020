package summer_practice_2020.purple.graphgen;

import summer_practice_2020.purple.IGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	private IGraph.Node genNode(IGraph g) {
		IGraph.Node n = g.addNode();
		n.setTitle(nameGen.generateName());
		return n;
	}

	private void genNodes(IGraph g) {
		for (int i = 0; i < nodesCount; i++) {
			genNode(g);
		}
	}
	
	private List<List<IGraph.Node>> genNodesInComponents(
			IGraph g, Iterable<Integer> counts) {
		List<List<IGraph.Node>> lists = new ArrayList<>();
		for (int count : counts) {
			List<IGraph.Node> nodes = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				nodes.add(genNode(g));
			}
			lists.add(nodes);
		}
		return lists;
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
	
	public void generateGraphComponents(IGraph g, Iterable<Integer> counts) {
		List<List<IGraph.Node>> nodeLists = genNodesInComponents(g, counts);
		for (List<IGraph.Node> nodes : nodeLists) {
			edgeGen.generateEdgesOnNodes(g, nodes);
		}
		genWeights(g);
	}
	
	// NOTE: ignores the generator's GraphEdgeGenerator
	public void generateGraphComponents(IGraph g, Iterable<Integer> counts,
			Iterable<GraphEdgeGenerator> gens) {
		Iterator<GraphEdgeGenerator> genIter = gens.iterator();
		List<List<IGraph.Node>> nodeLists = genNodesInComponents(g, counts);
		for (List<IGraph.Node> nodes : nodeLists) {
			genIter.next().generateEdgesOnNodes(g, nodes);
		}
		genWeights(g);
	}
}
