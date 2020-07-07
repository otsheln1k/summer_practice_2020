package summer_practice_2020.purple;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import summer_practice_2020.purple.IGraph.Edge;
import summer_practice_2020.purple.IGraph.Node;

class GraphTest {

	private static final Random rng = new Random();

	private static IGraph createEmptyGraph() {
		return new Graph();
	}

	private static int randomInt(int min, int max) {
		return rng.nextInt(max - min + 1) + min;
	}

	@Test
	void testAddRemoveNode() {
		IGraph g = createEmptyGraph();
		Node n = g.addNode();
		g.removeNode(n);
		assertThrows(NoSuchElementException.class,
				() -> g.removeNode(n));
	}

	@Test
	void testGetNodes() {
		IGraph g = createEmptyGraph();
		final int nNodes = randomInt(100, 200);
		Set<Node> nodes = new HashSet<>();

		for (int i = 0; i < nNodes; i++) {
			Node n = g.addNode();
			if (rng.nextBoolean()) {
				g.removeNode(n);
			} else {
				assertTrue(nodes.add(n));
			}
		}

		assertEquals(nodes.size(), g.nodesCount());

		for (Node n : g.getNodes()) {
			assertTrue(nodes.remove(n));
		}

		assertTrue(nodes.isEmpty());
	}

	@Test
	void testAddEdge() {
		IGraph g = createEmptyGraph();
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		Node n3 = g.addNode();
		Node n4 = g.addNode();
		Node n5 = g.addNode();
		Node n6 = g.addNode();

		g.removeNode(n3);
		g.removeNode(n5);
		g.removeNode(n6);

		g.addEdge(n1, n2);
		assertThrows(IllegalArgumentException.class,
				() -> g.addEdge(n1, n2));
		assertThrows(IllegalArgumentException.class,
				() -> g.addEdge(n2, n1));
		assertThrows(IllegalArgumentException.class,
				() -> g.addEdge(n3, n3));
		assertThrows(NoSuchElementException.class,
				() -> g.addEdge(n3, n4));
		assertThrows(NoSuchElementException.class,
				() -> g.addEdge(n5, n6));
	}

	@Test
	void testRemoveEdge() {
		IGraph g = createEmptyGraph();
		Node n1 = g.addNode();
		Node n2 = g.addNode();

		Edge e = g.addEdge(n1, n2);
		g.removeEdge(e);
		assertThrows(NoSuchElementException.class,
				() -> g.removeEdge(e));
	}

	@Test
	void testGetEdges() {
		IGraph g = createEmptyGraph();

		List<Node> nodes = new ArrayList<>();
		final int nNodes = randomInt(100, 200);
		for (int i = 0; i < nNodes; i++) {
			nodes.add(g.addNode());
		}

		Set<Edge> edges = new HashSet<>();
		for (int i = 0; i < nNodes; i++) {
			for (int j = i+1; j < nNodes; j++) {
				if (rng.nextBoolean()) {
					continue;
				}

				Edge e = g.addEdge(nodes.get(i), nodes.get(j));
				if (rng.nextBoolean()) {
					g.removeEdge(e);
				} else {
					assertTrue(edges.add(e));
				}
			}
		}

		assertEquals(edges.size(), g.edgesCount());

		for (Edge e : g.getEdges()) {
			assertTrue(edges.remove(e));
		}

		assertTrue(edges.isEmpty());
	}

	private static <T> boolean presentInIterable(T x, Iterable<T> it) {
		for (T y : it) {
			if (y == x) {
				return true;
			}
		}
		return false;
	}

	@Test
	void testRemoveNodeRemovesEdges() {
		IGraph g = createEmptyGraph();
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		Node n3 = g.addNode();
		Node n4 = g.addNode();

		Edge e12 = g.addEdge(n1, n2);
		Edge e34 = g.addEdge(n3, n4);

		g.removeNode(n1);
		assertFalse(presentInIterable(e12, g.getEdges()));
		assertTrue(presentInIterable(e34, g.getEdges()));
		assertEquals(1, g.edgesCount());

		g.removeNode(n4);
		assertFalse(presentInIterable(e12, g.getEdges()));
		assertFalse(presentInIterable(e34, g.getEdges()));
		assertEquals(0, g.edgesCount());
	}

	@Test
	void testGetEdgeBetween() {
		IGraph g = createEmptyGraph();
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		Node n3 = g.addNode();
		Edge e = g.addEdge(n1, n2);

		assertEquals(e, g.getEdgeBetween(n1, n2));
		assertEquals(e, g.getEdgeBetween(n2, n1));

		assertNull(g.getEdgeBetween(n1, n3));
		assertNull(g.getEdgeBetween(n3, n1));
		assertNull(g.getEdgeBetween(n2, n3));
		assertNull(g.getEdgeBetween(n3, n2));

		assertNull(g.getEdgeBetween(n1, n1));
		assertNull(g.getEdgeBetween(n2, n2));
		assertNull(g.getEdgeBetween(n3, n3));
	}

	private static void fillRandomGraph(IGraph g, int nNodes) {
		List<Node> nodes = new ArrayList<>();
		for (int i = 0; i < nNodes; i++) {
			nodes.add(g.addNode());
		}

		for (int i = 0; i < nNodes; i++) {
			for (int j = i+1; j < nNodes; j++) {
				if (rng.nextBoolean()) {
					g.addEdge(nodes.get(i), nodes.get(j));
				}
			}
		}
	}

	@Test
	void testEdgeNodeRefs() {
		IGraph g = createEmptyGraph();
		int nNodes = randomInt(50, 100);
		fillRandomGraph(g, nNodes);

		for (Edge e : g.getEdges()) {
			Node f = e.firstNode();
			Node s = e.secondNode();
			assertEquals(e, g.getEdgeBetween(f, s));
			assertEquals(e, g.getEdgeBetween(s, f));
		}
	}

	@Test
	void testGetEdgesFrom() {
		IGraph g = createEmptyGraph();
		int nNodes = randomInt(50, 100);
		fillRandomGraph(g, nNodes);

		for (Node n : g.getNodes()) {
			Set<Edge> realEdges = new HashSet<>();
			for (Node m : g.getNodes()) {
				Edge e = g.getEdgeBetween(n, m);
				if (e != null) {
					assertTrue(realEdges.add(e));
				}
			}

			for (Edge e : g.getEdgesFrom(n)) {
				assertTrue(realEdges.remove(e));
			}

			assertTrue(realEdges.isEmpty());
		}
	}

}
