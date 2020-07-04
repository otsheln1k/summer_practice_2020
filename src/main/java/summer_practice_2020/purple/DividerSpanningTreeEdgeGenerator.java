package summer_practice_2020.purple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DividerSpanningTreeEdgeGenerator implements GraphEdgeGenerator {
	private final Random rng = new Random();

	private void generateList(IGraph g, List<Graph.Node> s) {
		if (s.size() < 2) {
			return;
		}
		
		List<IGraph.Node> left =  new ArrayList<>();
		left.add(s.remove(s.size() - 1));
		List<IGraph.Node> right = new ArrayList<>();
		right.add(s.remove(s.size() - 1));

		for (IGraph.Node n : s) {
			List<IGraph.Node> dest = rng.nextBoolean() ? left : right;
			dest.add(n);
		}

		int leftIdx = rng.nextInt(left.size());
		int rightIdx = rng.nextInt(right.size());

		g.addEdge(left.get(leftIdx), right.get(rightIdx));
		generateList(g, left);
		generateList(g, right);
	}

	@Override
	public void generateEdges(IGraph g) {
		List<IGraph.Node> list = new ArrayList<>();
		g.getNodes().forEach(list::add);
		generateList(g, list);
	}

}
