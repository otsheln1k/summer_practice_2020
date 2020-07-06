package summer_practice_2020.purple.rendering;

import summer_practice_2020.purple.IGraph;

public class NodeList {
    private Node[] nodeList;
    private int size = 0;
    private int index = 0;

    public NodeList() {
        nodeList = new Node[this.size];
    }

    public NodeList(int size) {
        this.size = size;
        nodeList = new Node[this.size];
    }

    private void extend() {
        this.size += 10;
        Node[] tmp = new Node[size];
        if (this.index >= 0) {
            System.arraycopy(this.nodeList, 0, tmp, 0, this.index);
        }
        this.nodeList = tmp;
    }

    public void addNode(IGraph.Node node, double posx, double posy) {
        if (this.index == this.size) {
            this.extend();
        }
        this.nodeList[this.index++] = new Node(node, posx, posy);
    }

    public Node[] getNodeArray() {
        Node[] tmp = new Node[this.index];
        if (this.index >= 0) {
            System.arraycopy(this.nodeList, 0, tmp, 0, this.index);
        }
        return tmp;
    }
}
