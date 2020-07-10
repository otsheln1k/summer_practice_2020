package summer_practice_2020.purple;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphIOTest {
	
	private static IGraph createEmptyGraph() {
		return new Graph();
	}

	@Test
	void testWriteGraph() {
		IGraph g = createEmptyGraph();
		IGraph.Node n1 = g.addNode();
		IGraph.Node n2 = g.addNode();
		IGraph.Node n3 = g.addNode();
		n1.setTitle("A");
		n2.setTitle("B");
		n3.setTitle("C");
		g.addEdge(n1, n2).setWeight(1);
		g.addEdge(n1, n3).setWeight(2);
		g.addEdge(n3, n2).setWeight(3);

		ByteArrayOutputStream s = new ByteArrayOutputStream();
		GraphIO.writeGraph(s, g);

		InputStream is = new ByteArrayInputStream(s.toByteArray());
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		List<String> lines = new ArrayList<>();
		r.lines().forEach(lines::add);

		int nodes = 0;
		int edges = 0;
		for (String l : lines) {
			if (l.startsWith("node ")) {
				nodes++;
			} else if (l.startsWith("edge ")) {
				edges++;
			}
		}

		assertEquals(3, nodes);
		assertEquals(3, edges);
	}

	@Test
	void testReadGraph1() {
		final String input =
				"node a A\nnode b B\nnode c C\nedge a b 0.75\nedge a c 1.5\n";
		ByteArrayInputStream s = new ByteArrayInputStream(input.getBytes());

		IGraph g = createEmptyGraph();
		GraphIO.readGraph(s, g);

		assertEquals(3, g.nodesCount());
		assertEquals(2, g.edgesCount());

		Map<String, IGraph.Node> nodes = new HashMap<>();
		g.getNodes().forEach(n -> nodes.put(n.getTitle(), n));

		IGraph.Node nA = nodes.get("A");
		assertNotNull(nA);
		IGraph.Node nB = nodes.get("B");
		assertNotNull(nB);
		IGraph.Node nC = nodes.get("C");
		assertNotNull(nC);

		assertEquals(0.75, g.getEdgeBetween(nB, nA).getWeight());
		assertEquals(1.5, g.getEdgeBetween(nA, nC).getWeight());
		assertNull(g.getEdgeBetween(nB, nC));
	}
	
	// TODO: tests with invalid input

}
