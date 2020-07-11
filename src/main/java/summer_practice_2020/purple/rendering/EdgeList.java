package summer_practice_2020.purple.rendering;

import javafx.scene.paint.Color;
import summer_practice_2020.purple.IGraph;

public class EdgeList {
    private Edge[] edgeList;
    private int size;
    private int index = 0;

    public EdgeList(int size) {
        this.size = size;
        edgeList = new Edge[this.size];
    }

    private void extend() {
        this.size += 10;
        Edge[] tmp = new Edge[size];
        if (this.index >= 0) {
            System.arraycopy(this.edgeList, 0, tmp, 0, this.index);
        }
        this.edgeList = tmp;
    }

    public void addEdge(IGraph.Edge edge, Node[] nodeList, Color color) {
        if (this.index == this.size) {
            this.extend();
        }
        Node node1 = null;
        Node node2 = null;
        for (int i = 0; (node1 == null || node2 == null) && i < nodeList.length; i++) {
            if (edge.firstNode().equals(nodeList[i].getNode())) {
                node1 = nodeList[i];
            } else if (edge.secondNode().equals(nodeList[i].getNode())) {
                node2 = nodeList[i];
            }
        }
        this.edgeList[this.index++] = new Edge(edge, node1, node2, color);
    }

    public Edge[] getEdgeArray() {
        Edge[] tmp = new Edge[this.index];
        System.arraycopy(this.edgeList, 0, tmp, 0, this.index);
        return tmp;
    }
}
