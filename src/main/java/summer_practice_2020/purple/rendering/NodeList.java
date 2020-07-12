package summer_practice_2020.purple.rendering;

public class NodeList {
    private Node[] nodeList;
    private int size;
    private int index = 0;

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

    public void addNode(Node n) {
        if (this.index == this.size) {
            this.extend();
        }
        this.nodeList[this.index++] = n;
    }

    public Node[] getNodeArray() {
        Node[] tmp = new Node[this.index];
        if (this.index >= 0) {
            System.arraycopy(this.nodeList, 0, tmp, 0, this.index);
        }
        return tmp;
    }
}
