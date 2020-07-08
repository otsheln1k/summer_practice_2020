package summer_practice_2020.purple.graphgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import summer_practice_2020.purple.IGraph;

public class ShuffleGraphEdgeGenerator implements GraphEdgeGenerator {

	private final int edgesCount;
	private final Random rng = new Random();

	public ShuffleGraphEdgeGenerator(int edgesCount) {
		this.edgesCount = edgesCount;
	}

	private class EdgeCandidate {
		private final IGraph.Node a;
		private final IGraph.Node b;

		public EdgeCandidate(IGraph.Node a, IGraph.Node b) {
			this.a = a;
			this.b = b;
		}

		public IGraph.Node getFirstNode() {
			return a;
		}

		public IGraph.Node getSecondNode() {
			return b;
		}
	}

	@Override
	public void generateEdgesOnNodes(IGraph g, Iterable<IGraph.Node> ns) {
		List<IGraph.Node> nodes = new ArrayList<>();
		ns.forEach(nodes::add);

		List<EdgeCandidate> possibleEdges = new ArrayList<>();
		for (int i = 0; i < nodes.size(); i++) {
			IGraph.Node ni = nodes.get(i);
			for (int j = i+1; j < nodes.size(); j++) {
				IGraph.Node nj = nodes.get(j);
				if (g.getEdgeBetween(ni, nj) != null) {
					continue;
				}

				possibleEdges.add(new EdgeCandidate(ni, nj));
			}
		}

		Collections.shuffle(possibleEdges, rng);

		int realEdgesCount = Math.min(edgesCount, possibleEdges.size());

		for (int i = 0; i < realEdgesCount; i++) {
			EdgeCandidate e = possibleEdges.get(i);
			g.addEdge(e.getFirstNode(), e.getSecondNode());
		}
	}

}
