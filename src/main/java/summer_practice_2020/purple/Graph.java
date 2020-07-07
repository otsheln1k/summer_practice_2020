package summer_practice_2020.purple;

import java.util.*;

public class Graph implements IGraph {


    private final HashMap<Node, Map<Node, Integer>> vertexMap = new HashMap<>();
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
        vertexMap.put(n, new HashMap<>());
        return n;
    }

    @Override
    public void removeNode(Node node) {
        vertexMap.remove(node, vertexMap.get(node));
        Iterator<Edge> i = edges.iterator();
        Edge e;
        while (i.hasNext()) {
            e = i.next();
            if (e.firstNode().equals(node)) {
                edges.remove(e);
            }
        }
    }

    @Override
    public Iterable<Node> getNodes() {
        return vertexMap.keySet();
    }

    @Override
    public int nodesCount() {
        return vertexMap.size();
    }

    @Override
    public Edge addEdge(Node a, Node b) {
        Edge e = new DEdge(a, b);
        edges.add(e);
        return e;
    }

    @Override
    public void removeEdge(Edge edge) {
        edges.remove(edge);
        Node now_first_node = edge.firstNode();
        Node now_second_node = edge.secondNode();
        vertexMap.get(now_first_node).remove(now_second_node, vertexMap.get(now_first_node).get(now_second_node));
    }

    @Override
    public Edge getEdgeBetween(Node a, Node b) {
        Iterator<Edge> i = edges.iterator();
        Edge e;
        while (i.hasNext()) {
            e = i.next();
            if (e.firstNode().equals(a)) {
                if (e.secondNode().equals(b)) {
                    return e;
                }
            }
        }
        //Вернуть исключение???
        e = null;
        return e;
    }

    @Override
    public Iterable<Edge> getEdgesFrom(Node node) {
        Set<Edge> node_edges = new HashSet<>();
        Iterator<Edge> i = edges.iterator();
        Edge e;
        while (i.hasNext()) {
            e = i.next();
            if (e.firstNode().equals(node)) {
                node_edges.add(e);
            }
        }
        return node_edges;
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

    public Integer getSize() {
        return vertexMap.size();
    }
}