package summer_practice_2020.purple.graphgen;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import summer_practice_2020.purple.Graph;
import summer_practice_2020.purple.IGraph;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphGenerationTest {

    static final Random rng = new Random();

    private static int randomInt(int min, int max) {
        return rng.nextInt(max - min + 1) + min;
    }

    private static IGraph createEmptyGraph() {
        return new Graph();
    }

    private Set<IGraph.Node> reachableNodes(IGraph g, IGraph.Node start) {
        Set<IGraph.Node> s = new HashSet<>();
        s.add(start);
        Queue<IGraph.Node> q = new ArrayDeque<>();
        q.add(start);

        while (!q.isEmpty()) {
            IGraph.Node i = q.remove();
            for (IGraph.Edge e : g.getEdgesFrom(i)) {
                IGraph.Node fn = e.firstNode();
                IGraph.Node sn = e.secondNode();
                IGraph.Node o = (fn == i) ? sn : fn;
                if (s.add(o)) {
                    q.add(o);
                }
            }
        }

        return s;
    }

    @Test
    void testDividerSpanningTree() {
        final int count = 25;

        IGraph g = createEmptyGraph();
        IGraph.Node n = g.addNode();
        for (int i = 0; i < count - 1; i++) {
            g.addNode();
        }

        GraphEdgeGenerator gen = new DividerSpanningTreeEdgeGenerator();
        gen.generateEdges(g);

        assertEquals(count, g.nodesCount());
        assertEquals(count - 1, g.edgesCount());

        Set<IGraph.Node> s = reachableNodes(g, n);
        assertEquals(count, s.size());
    }

    @Test
    void testNoDuplicateEdgesInShuffleGenerator() {
        final int nNodes = 10;

        IGraph g = createEmptyGraph();
        for (int i = 0; i < nNodes; i++) {
            g.addNode();
        }

        // should only generate nNodes*(nNodes-1)/2
        GraphEdgeGenerator gen =
                new ShuffleGraphEdgeGenerator(nNodes * (nNodes - 1));

        gen.generateEdges(g);

        assertEquals(nNodes * (nNodes - 1) / 2, g.edgesCount());
    }

    @Test
    void testTwoSubgraphs() {
        final int size1 = 20;
        final int size2 = 20;

        IGraph g = createEmptyGraph();

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

        Set<IGraph.Node> s1 = reachableNodes(g, nodes1.get(0));
        assertEquals(size1, s1.size());

        Set<IGraph.Node> s2 = reachableNodes(g, nodes2.get(0));
        assertEquals(size2, s2.size());
    }

    private List<Set<IGraph.Node>> getNodeSets(IGraph g) {
        Set<IGraph.Node> visitedNodes = new HashSet<>();
        List<Set<IGraph.Node>> nodeSets = new ArrayList<>();

        for (IGraph.Node n : g.getNodes()) {
            if (visitedNodes.contains(n)) {
                continue;
            }

            Set<IGraph.Node> nodes = reachableNodes(g, n);
            nodeSets.add(nodes);

            for (IGraph.Node m : nodes) {
                assertTrue(visitedNodes.add(m));
            }
        }

        return nodeSets;
    }

    @RepeatedTest(10)
    void testSubgraphsFromFacade() {
        final int nodesCount = randomInt(50, 400);
        final int compsCount = randomInt(5, 50);

        IGraph g = createEmptyGraph();
        GraphGeneratorFacade gen = new GraphGeneratorFacade();
        gen.generateComponents(g, nodesCount, compsCount);

        assertEquals(nodesCount, g.nodesCount());

        List<Set<IGraph.Node>> nodeSets = getNodeSets(g);
        assertEquals(compsCount, nodeSets.size());
        assertEquals(nodesCount, nodeSets.stream().mapToInt(Set::size).sum());
    }

    private Set<IGraph.Edge> nodeSetEdges(IGraph g, Set<IGraph.Node> nodes) {
        Set<IGraph.Edge> edges = new HashSet<>();
        for (IGraph.Node n : nodes) {
            for (IGraph.Edge e : g.getEdgesFrom(n)) {
                edges.add(e);
            }
        }
        return edges;
    }

    @RepeatedTest(10)
    void testComponentsWithNEdges() {
        final int nodesCount = randomInt(100, 200);
        final int compsCount = randomInt(3, 10);
        final int moreEdges = randomInt(1, 200);

        final int edgesCount = nodesCount - compsCount + moreEdges;

        IGraph g = createEmptyGraph();
        GraphGeneratorFacade gen = new GraphGeneratorFacade();
        gen.generateComponentsWithNEdges(g, nodesCount, edgesCount, compsCount);

        assertEquals(nodesCount, g.nodesCount());
        assertEquals(edgesCount, g.edgesCount());

        List<Set<IGraph.Node>> nodeSets = getNodeSets(g);
        assertEquals(compsCount, nodeSets.size());

        for (Set<IGraph.Node> nodes : nodeSets) {
            Set<IGraph.Edge> edges = nodeSetEdges(g, nodes);
            int size = edges.size();
            assertTrue(size <= nodes.size() - 1 + moreEdges);
        }
    }

    @Test
    void testNoDuplicateEdgesInSimpleGenerator() {
        final int nNodes = 10;

        IGraph g = createEmptyGraph();
        for (int i = 0; i < nNodes; i++) {
            g.addNode();
        }

        // should only generate nNodes*(nNodes-1)/2
        GraphEdgeGenerator gen = new SimpleGraphEdgeGenerator(1.0);

        gen.generateEdges(g);

        assertEquals(nNodes * (nNodes - 1) / 2, g.edgesCount());
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

        assertEquals(String.valueOf((char) ('A' + 1 + skip)), gen.generateName());

        final int skip2 = alpha_size - 2 - skip;
        for (int i = 0; i < skip2; i++) {
            gen.generateName();
        }

        assertEquals("AA", gen.generateName());

        for (int i = 0; i < alpha_size - 1; i++) {
            gen.generateName();
        }

        assertEquals("BA", gen.generateName());

        final int skip3 = alpha_size * (alpha_size - 1) - 2;
        for (int i = 0; i < skip3; i++) {
            gen.generateName();
        }

        assertEquals("ZZ", gen.generateName());
        assertEquals("AAA", gen.generateName());
    }

}
