package summer_practice_2020.purple;

import java.util.*;

public class Graph implements IGraph {


    private final HashMap<Node, Map<Node, Integer>> vertexMap = new HashMap<Node, Map<Node, Integer>>();
    private final Set<Edge> edges = new HashSet<>();

    private static class DNode implements IGraph.Node {
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
        vertexMap.put(n, new HashMap<Node, Integer>());
        return n;
    }

    @Override
    public void removeNode(Node node) {

    }

    @Override
    public Iterable<Node> getNodes() {
        return (new ArrayList(vertexMap.keySet()));
    }

    @Override
    public int nodesCount() {
        return vertexMap.size();
    }

    @Override
    public Edge addEdge(Node a, Node b) {
        Edge e = new DEdge(a, b);
        vertexMap.get(a).put(b, 0); ///!!!!!?????
        edges.add(e);
        return e;
    }

    @Override
    public void removeEdge(Edge edge) {
    }

    @Override
    public Edge getEdgeBetween(Node a, Node b) {
        return null;
    }

    @Override
    public Iterable<Edge> getEdgesFrom(Node node) {
        return null;
    }

    @Override
    public Iterable<Edge> getEdges() {
        return edges;
    }

    @Override
    public int edgesCount() {
        return edges.size();
    }

    public Map<Node, Map<Node, Integer>> getVertexMap() {
        return vertexMap;
    }

    public Integer getSize(){
        return vertexMap.size();
    }
}
