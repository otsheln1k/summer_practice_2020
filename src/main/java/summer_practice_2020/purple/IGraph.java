package summer_practice_2020.purple;

    public interface IGraph {
        interface Edge {
            void setWeight(double w);
            double getWeight();
            Node firstNode();
            Node secondNode();

            Node otherNode(Node n);
        }

        interface Node {
            void setTitle(String t);
            String getTitle();
        }

        Node addNode();
        void removeNode(Node node);

        Iterable<Node> getNodes();
        int nodesCount();

        Edge addEdge(Node a, Node b);
        void removeEdge(Edge edge);

        Edge getEdgeBetween(Node a, Node b);
        Iterable<Edge> getEdgesFrom(Node node);
        Iterable<Edge> getEdges();
        int edgesCount();
    }



