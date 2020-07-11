package summer_practice_2020.purple;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class Graph implements IGraph {

    private final Set<Node> nodes = new HashSet<>();
    private final Set<Edge> edges = new HashSet<>();

    private static class DNode implements IGraph.Node {
        private String title = "";
        private double posX = -1;
        private double posY = -1;

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public void setPosX(double posX) {
            this.posX = posX;
        }

        @Override
        public void setPosY(double posY) {
            this.posY = posY;
        }

        @Override
        public double getPosX() {
            return this.posX;
        }

        @Override
        public double getPosY() {
            return posY;
        }
    }

    private static class DEdge implements IGraph.Edge {
        private double weight = 0.0;
        private final Node a;
        private final Node b;

        public DEdge(Node a, Node b) {
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


    @Override
    public Node addNode() {
        Node n = new DNode();
        nodes.add(n);
        return n;
    }

    @Override
    public void removeNode(Node node) {
        if (nodes.contains(node)) {
            nodes.remove(node);
            for (Edge e : getEdgesFrom(node)) {
                removeEdge(e);
            }
        } else
            throw new NoSuchElementException();
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
            throw new IllegalArgumentException();
        } else {
            if (getEdgeBetween(a, b) != null | getEdgeBetween(b, a) != null) {
                throw new IllegalArgumentException();
            } else {
                if (!nodes.contains(a) || !nodes.contains(b)) {
                    throw new NoSuchElementException();
                } else {
                    Edge e = new DEdge(a, b);
                    edges.add(e);
                    return e;
                }
            }
        }
    }

    @Override
    public void removeEdge(Edge edge) {
        if (edges.contains(edge))
            edges.remove(edge);
        else
            throw new NoSuchElementException();
    }

    @Override
    public Edge getEdgeBetween(Node a, Node b) {

        for (Edge e : edges) {
            if ((e.firstNode() == a && e.secondNode() == b) || (e.firstNode() == b && e.secondNode() == a)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public Iterable<Edge> getEdgesFrom(Node node) {
        Set<Edge> fromNode = new HashSet<>();
        for (Edge e : edges) {
            if (e.firstNode() == node || e.secondNode() == node) {
                fromNode.add(e);
            }
        }
        return fromNode;
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