package summer_practice_2020.purple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import summer_practice_2020.purple.util.FilteredIterator;

public class SimpleGraph implements IGraph {

    private static class SimpleNode implements IGraph.Node {
        private String title = "";

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setTitle(String title) {
            this.title = title;
        }

    }

    private static class SimpleEdge implements IGraph.Edge {
        private double weight = 0.0;
        private final Node a;
        private final Node b;

        public SimpleEdge(Node a, Node b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void setWeight(double w) {
            weight = w;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public Node firstNode() {
            return a;
        }

        @Override
        public Node secondNode() {
            return b;
        }

    }

    private final Set<Node> nodes = new HashSet<>();
    private final Set<Edge> edges = new HashSet<>();

    @Override
    public Node addNode() {
        Node n = new SimpleNode();
        nodes.add(n);
        return n;
    }

    @Override
    public void removeNode(Node node) {
        if (!nodes.remove(node)) {
            throw new NoSuchElementException(
                    "cannot remove nonexistant node");
        }

        Iterator<Edge> iter = edges.iterator();
        while (iter.hasNext()) {
            Edge e = iter.next();
            if (e.firstNode() == node || e.secondNode() == node) {
                iter.remove();
            }
        }
    }

    @Override
    public Iterable<Node> getNodes() {
        return nodes;
    }

    @Override
    public int nodesCount() {
        return nodes.size();
    }

    @Override
    public Edge addEdge(Node a, Node b) {
        if (a == b) {
            throw new IllegalArgumentException(
                    "tried to add a loop edge");
        }
        if (!nodes.contains(a) || !nodes.contains(b)) {
            throw new NoSuchElementException(
                    "cannot add edge to nonexistant node");
        }
        if (getEdgeBetween(a, b) != null) {
            throw new IllegalArgumentException(
                    "tried to add a duplicate edge");
        }
        Edge e = new SimpleEdge(a, b);
        edges.add(e);
        return e;
    }

    @Override
    public void removeEdge(Edge edge) {
        if (!edges.remove(edge)) {
            throw new NoSuchElementException(
                    "cannot remove nonexistant edge");
        }
    }

    @Override
    public Edge getEdgeBetween(Node a, Node b) {
        return edges.stream()
                .filter(e -> {
                    return e.firstNode() == a && e.secondNode() == b
                            || e.firstNode() == b && e.secondNode() == a;
                }).findFirst().orElse(null);
    }

    @Override
    public Iterable<Edge> getEdgesFrom(Node node) {
        return () -> new FilteredIterator<>(edges.iterator(),
                e -> e.firstNode() == node || e.secondNode() == node);
    }

    @Override
    public Iterable<Edge> getEdges() {
        return edges;
    }

    @Override
    public int edgesCount() {
        return edges.size();
    }

}