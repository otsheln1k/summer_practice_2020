package summer_practice_2020.purple;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

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
		int pos = 0;
		for (String l : lines) {
			if (l.startsWith("node ")) {
				nodes++;
			} else if (l.startsWith("edge ")) {
				edges++;
			} else if (l.startsWith("pos ")) {
				pos++;
			}
		}

		assertEquals(3, nodes);
		assertEquals(nodes, pos);
		assertEquals(3, edges);
	}

	@Test
	void testReadGraph() {
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

		assertEquals(0.75, g.getEdgeBetween(nB, nA).getWeight());
		assertEquals(1.5, g.getEdgeBetween(nA, nC).getWeight());
		assertNull(g.getEdgeBetween(nB, nC));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"edge not existant 1.0\n",
			"node a A\nnode b B\nedge a b foo\n",
			"node a A\nnode a B\n",
			"node a\npos a x y",
			"node a\nfoo b\n",
	})
	void testGraphFormatException(String input) {
		ByteArrayInputStream s = new ByteArrayInputStream(input.getBytes());
		IGraph g = createEmptyGraph();
		assertThrows(GraphFormatException.class,
				() -> GraphIO.readGraph(s, g));
	}

	static Stream<Arguments> validInputsProvider() {
		return Stream.of(
				arguments("node 1 A\nnode 2 B\n", 2, 0),
				arguments("node a\nnode b\n", 2, 0),
				arguments("node a\nnode b B\n", 2, 0),
				arguments("node a", 1, 0),
				arguments("node a A\nnode b A\n", 2, 0),
				arguments("node a A\nnode b B\nedge a b -1\n", 2, 1),
				arguments("node\ta\tA\rnode\tb\tB\r", 2, 0),
				arguments("node a\npos a -100 0.0005", 1, 0),
				arguments("node a text with spaces\nnode b\n", 2, 0)
				);
	}

	@ParameterizedTest
	@MethodSource("validInputsProvider")
	void testValidInputs(String input, int nNodes, int nEdges) {
		ByteArrayInputStream s = new ByteArrayInputStream(input.getBytes());
		IGraph g = createEmptyGraph();
		assertDoesNotThrow(() -> GraphIO.readGraph(s, g));
		assertEquals(nNodes, g.nodesCount());
		assertEquals(nEdges, g.edgesCount());
	}

}
