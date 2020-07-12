package summer_practice_2020.purple;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import summer_practice_2020.purple.IGraph.Edge;
import summer_practice_2020.purple.IGraph.Node;
import summer_practice_2020.purple.boruvka.Boruvka;
import summer_practice_2020.purple.graphgen.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BoruvkaTest {

    private static final Random rng = new Random();

    private static int randomInt(int min, int max) {
        return rng.nextInt(max - min + 1) + min;
    }

    private static IGraph createEmptyGraph() {
        return new Graph();
    }

    private static void generateConnectedGraph(IGraph g, int nodesCount) {
        GraphNodeNameGenerator ngen = new AlphabetNodeNameGenerator();
        GraphEdgeWeightGenerator wgen = () -> rng.nextInt(100);
        GraphEdgeGenerator gen1 = new DividerSpanningTreeEdgeGenerator();
        GraphEdgeGenerator gen2 = new SimpleGraphEdgeGenerator(0.8);

        for (int i = 0; i < nodesCount; i++) {
            g.addNode();
        }
        gen1.generateEdges(g);
        gen2.generateEdges(g);
        for (Node n : g.getNodes()) {
            n.setTitle(ngen.generateName());
        }
        for (Edge e : g.getEdges()) {
            e.setWeight(wgen.generateWeight());
        }
    }

    private static Node someNode(IGraph g) {
        return g.getNodes().iterator().next();
    }

    private static Set<Node> reachableNodes(IGraph g, Set<Edge> edges, Node start) {
        Set<Node> nodes = new HashSet<>();
        nodes.add(start);

        Queue<Node> q = new LinkedList<>();
        q.add(start);

        while (!q.isEmpty()) {
            Node n = q.remove();
            for (Edge e : edges) {
                Node d;
                if (e.firstNode() == n) {
                    d = e.secondNode();
                } else if (e.secondNode() == n) {
                    d = e.firstNode();
                } else {
                    continue;
                }
                if (nodes.add(d)) {
                    q.add(d);
                }
            }
        }

        return nodes;
    }

    @Test
    void testGraphUnmodified() {
        final int nNodes = 50;
        IGraph g = createEmptyGraph();
        generateConnectedGraph(g, nNodes);

        List<Double> weights = new ArrayList<>();
        for (Edge e : g.getEdges()) {
            weights.add(e.getWeight());
        }

        Boruvka b = new Boruvka(g);
        b.boruvka();

        int i = 0;
        for (Edge e : g.getEdges()) {
            assertEquals(weights.get(i++), e.getWeight());
        }
    }

    @RepeatedTest(5)
    void testResultIsSpanningTree() {
        final int nNodes = randomInt(50, 100);
        IGraph g = createEmptyGraph();
        generateConnectedGraph(g, nNodes);

        Boruvka b = new Boruvka(g);
        b.boruvka();
        Set<Edge> edges = b.resultEdgeSet();

        assertEquals(nNodes - 1, edges.size());

        Set<Node> nodes = reachableNodes(g, edges, someNode(g));
        assertEquals(nNodes, nodes.size());
    }

    private static double totalWeight(Iterable<Edge> edges) {
        double sum = 0;
        for (Edge e : edges) {
            sum += e.getWeight();
        }
        return sum;
    }

    // needed to test the algorithm
    private static class CombinationsIterator<T> implements Iterator<Set<T>> {

        private List<Integer> next = new ArrayList<>();
        private final List<T> elts;
        private final int size;

        public CombinationsIterator(List<T> elts, int size) {
            if (size < 0 || size > elts.size()) {
                throw new IllegalArgumentException(
                        "requested size negative or greater than number of elements");
            }

            this.elts = elts;
            this.size = size;

            for (int i = 0; i < size; i++) {
                next.add(i);
            }
        }

        private Set<T> makeCurrentSet() {
            Set<T> s = new HashSet<>();
            for (int i = 0; i < size; i++) {
                s.add(elts.get(next.get(i)));
            }
            return s;
        }

        private boolean advance() {
            int i;
            for (i = 0; i < size; i++) {
                if (next.get(size - i - 1) < elts.size() - i - 1) {
                    break;
                }
            }
            if (i == size) {
                return false;
            }

            int pos = size - i - 1;
            int low = next.get(pos) + 1;
            next.set(pos, low);

            for (int j = 0; j + pos < size; j++) {
                next.set(pos + j, low + j);
            }

            return true;
        }

        @Override
        public Set<T> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Set<T> s = makeCurrentSet();
            if (!advance()) {
                next = null;
            }
            return s;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

    }

    @Test
    void testCombinationsIterator() {
        final List<Integer> answer = new ArrayList<>();
        final int n = 5;
        final int k = 3;
        Collections.addAll(answer,
                7, 11, 19, 13, 21, 25, 14, 22, 26, 28);

        List<Integer> elts = new ArrayList<>();
        for (int i = 0, next = 1; i < n; i++, next *= 2) {
            elts.add(next);
        }

        Iterator<Set<Integer>> iter =
                new CombinationsIterator<>(elts, k);

        // trick to get iterator from stream
        int[] res = new int[10];
        int count = 0;

        while (iter.hasNext()) {
            Set<Integer> s = iter.next();
            int sum = s.stream().mapToInt(Integer::intValue).sum();
            if (res.length == count) res = Arrays.copyOf(res, count * 2);
            res[count++] = sum;
        }
        res = Arrays.copyOfRange(res, 0, count);

        assertEquals(answer.size(), res.length);

        for (int i = 0; i < res.length; i++) {
            assertEquals(answer.get(i), res[i]);
        }
    }

    @RepeatedTest(5)
    void testResultIsOptimal() {
        final int nNodes = randomInt(5, 8);
        IGraph g = createEmptyGraph();
        generateConnectedGraph(g, nNodes);
        Node start = someNode(g);

        Boruvka b = new Boruvka(g);
        b.boruvka();
        Set<Edge> edges = b.resultEdgeSet();

        assertEquals(nNodes - 1, edges.size());

        double answerWeight = totalWeight(edges);

        List<Edge> allEdges = new ArrayList<>();
        g.getEdges().forEach(allEdges::add);

        Iterator<Set<Edge>> iter =
                new CombinationsIterator<>(allEdges, nNodes - 1);

        while (iter.hasNext()) {
            Set<Edge> tryEdges = iter.next();

            Set<Node> nodes = reachableNodes(g, tryEdges, start);
            if (nodes.size() != nNodes) {
                continue;
            }

            double weight = totalWeight(tryEdges);
            assertFalse(weight < answerWeight);
        }
    }


    @Test
    void testNoEdges() {
        final int nNodes = 100;
        IGraph g = createEmptyGraph();
        for (int i = 0; i < nNodes; i++) {
            g.addNode();
        }

        Boruvka b = new Boruvka(g);
        b.boruvka();

        assertEquals(nNodes, g.nodesCount());
        assertEquals(0, b.resultEdgeSet().size());
    }

    private void generateComponentGraph(IGraph g, int nNodes, int nComps) {
        GraphGeneratorFacade gen = new GraphGeneratorFacade();
        gen.generateComponents(g, nNodes, nComps);
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
    void testComponentsResultIsSpanningTree() {
        final int nNodes = randomInt(50, 400);
        final int nComps = randomInt(5, 50);
        IGraph g = createEmptyGraph();
        generateComponentGraph(g, nNodes, nComps);

        Boruvka b = new Boruvka(g);
        b.boruvka();
        Set<Edge> edges = b.resultEdgeSet();

        Set<Edge> realEdges = new HashSet<>();
        g.getEdges().forEach(realEdges::add);

        Set<Node> nodes = new HashSet<>();
        List<Set<Edge>> edgesSets = new ArrayList<>();
        for (Node n : g.getNodes()) {
            if (!nodes.contains(n)) {
                Set<Node> comp = reachableNodes(g, edges, n);
                Set<Node> realComp = reachableNodes(g, realEdges, n);

                assertEquals(realComp, comp);

                for (Node m : comp) {
                    assertTrue(nodes.add(m));
                }
                Set<Edge> compEdges = nodeSetEdges(g, comp);
                edgesSets.add(compEdges);
            }
        }

        assertEquals(nComps, edgesSets.size());

        int totalEdges = edgesSets.stream().mapToInt(Set::size).sum();
        assertEquals(g.edgesCount(), totalEdges);

        // Test that there are no common edges
        for (int i = 0; i < edgesSets.size(); i++) {
            for (int j = i + 1; j < edgesSets.size(); j++) {
                for (Edge e : edgesSets.get(j)) {
                    assertFalse(edgesSets.get(i).remove(e));
                }
            }
        }
    }

}
