package summer_practice_2020.purple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
	public void generateEdges(IGraph g) {
		List<IGraph.Node> nodes = new ArrayList<>();
		g.getNodes().forEach(nodes::add);
		
		List<EdgeCandidate> possibleEdges = new ArrayList<>();
		for (int i = 0; i < nodes.size(); i++) {
			IGraph.Node ni = nodes.get(i);
			for (int j = 0; j < nodes.size(); j++) {
				IGraph.Node nj = nodes.get(j);
				if (g.getEdgeBetween(ni, nj) != null) {
					continue;
				}
				
				possibleEdges.add(new EdgeCandidate(ni, nj));
			}
		}
		
		Collections.shuffle(possibleEdges, rng);
		
		for (int i = 0; i < edgesCount; i++) {
			EdgeCandidate e = possibleEdges.get(i);
			g.addEdge(e.getFirstNode(), e.getSecondNode());
		}
	}

}
