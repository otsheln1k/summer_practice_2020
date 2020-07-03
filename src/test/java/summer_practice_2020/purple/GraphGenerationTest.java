package summer_practice_2020.purple;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Test;

class GraphGenerationTest {

	@Test
	void testDividerSpanningTree() {
		final int count = 25;

		Graph g = new SimpleGraph();
		Graph.Node n = g.addNode();
		for (int i = 0; i < count-1; i++) {
			g.addNode();
		}

		GraphEdgeGenerator gen = new DividerSpanningTreeEdgeGenerator();
		gen.generateEdges(g);

		assertEquals(count, g.nodesCount());
		assertEquals(count - 1, g.edgesCount());

		Set<Graph.Node> s = new HashSet<>();
		Queue<Graph.Node> q = new ArrayDeque<>();
		q.add(n);
		while (!q.isEmpty()) {
			Graph.Node i = q.remove();
			for (Graph.Edge e : g.getEdgesFrom(i)) {
				Graph.Node o =
						(e.firstNode() == i) ? e.secondNode() : e.firstNode();
				if (s.add(o)) {
					q.add(o);
				}
			}
		}

		assertEquals(count, s.size());
	}
	
	@Test
	void testAlphabetNameGenerator() {
		final int alpha_size = 26;
		
		GraphNodeNameGenerator gen = new AlphabetNodeNameGenerator();
		assertEquals("A", gen.generateName());
		
		final int skip = 15;
		for (int i = 0; i < skip; i++) {
			gen.generateName();
		}
		
		assertEquals(String.valueOf((char)('A'+1+skip)), gen.generateName());
		
		final int skip2 = alpha_size-2-skip;
		for (int i = 0; i < skip2; i++) {
			gen.generateName();
		}
		
		assertEquals("AA", gen.generateName());
		
		for (int i = 0; i < alpha_size-1; i++) {
			gen.generateName();
		}
		
		assertEquals("BA", gen.generateName());
		
		final int skip3 = alpha_size * (alpha_size-1) - 2;
		for (int i = 0; i < skip3; i++) {
			gen.generateName();
		}
		
		assertEquals("ZZ", gen.generateName());
		assertEquals("AAA", gen.generateName());
	}

}
