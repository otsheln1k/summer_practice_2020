package summer_practice_2020.purple;

    public interface IGraph {
        public interface Edge {
            void setWeight(double w);
            double getWeight();
            Node firstNode();
            Node secondNode();
        }

        public interface Node {
            void setTitle(String t);
            String getTitle();
        }

        public Node addNode();
        public void removeNode(Node node);

        public Iterable<Node> getNodes();
        public int nodesCount();

        public Edge addEdge(Node a, Node b);
        public void removeEdge(Edge edge);

        public Edge getEdgeBetween(Node a, Node b);
        public Iterable<Edge> getEdgesFrom(Node node);
        public Iterable<Edge> getEdges();
        public int edgesCount();
    }



