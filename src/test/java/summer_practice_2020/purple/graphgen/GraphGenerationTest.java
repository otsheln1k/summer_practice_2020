package summer_practice_2020.purple.graphgen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Test;

import summer_practice_2020.purple.IGraph;
import summer_practice_2020.purple.SimpleGraph;

class GraphGenerationTest {

	@Test
	void testDividerSpanningTree() {
		final int count = 25;

		IGraph g = new SimpleGraph();
		IGraph.Node n = g.addNode();
		for (int i = 0; i < count-1; i++) {
			g.addNode();
		}

		GraphEdgeGenerator gen = new DividerSpanningTreeEdgeGenerator();
		gen.generateEdges(g);

		assertEquals(count, g.nodesCount());
		assertEquals(count - 1, g.edgesCount());

		Set<IGraph.Node> s = new HashSet<>();
		Queue<IGraph.Node> q = new ArrayDeque<>();
		q.add(n);
		while (!q.isEmpty()) {
			IGraph.Node i = q.remove();
			for (IGraph.Edge e : g.getEdgesFrom(i)) {
				IGraph.Node o =
						(e.firstNode() == i) ? e.secondNode() : e.firstNode();
						if (s.add(o)) {
							q.add(o);
						}
			}
		}

		assertEquals(count, s.size());
	}

	@Test
	void testNoDuplicateEdges() {
		final int nNodes = 10;

		IGraph g = new SimpleGraph();
		for (int i = 0; i < nNodes; i++) {
			g.addNode();
		}

		// should only generate nNodes*(nNodes-1)/2
		GraphEdgeGenerator gen =
				new ShuffleGraphEdgeGenerator(nNodes*(nNodes-1));

		gen.generateEdges(g);

		assertEquals(nNodes*(nNodes-1)/2, g.edgesCount());
	}
	
	@Test
	void testTwoSubgraphs() {
		final int size1 = 20;
		final int size2 = 20;
		
		IGraph g = new SimpleGraph();
		
		List<IGraph.Node> nodes1 = new ArrayList<>();
		for (int i = 0; i < size1; i++) {
			nodes1.add(g.addNode());
		}
		
		List<IGraph.Node> nodes2 = new ArrayList<>();
		for (int i = 0; i < size2; i++) {
			nodes2.add(g.addNode());
		}
		
		GraphEdgeGenerator gen = new DividerSpanningTreeEdgeGenerator();
		gen.generateEdgesOnNodes(g, nodes1);
		gen.generateEdgesOnNodes(g, nodes2);
		
		assertEquals(size1 + size2 - 2, g.edgesCount());
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
