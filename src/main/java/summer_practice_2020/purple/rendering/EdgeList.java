package summer_practice_2020.purple.rendering;

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

    public void addEdge(Edge e) {
        if (this.index == this.size) {
            this.extend();
        }
        this.edgeList[this.index++] = e;
    }

    public Edge[] getEdgeArray() {
        Edge[] tmp = new Edge[this.index];
        System.arraycopy(this.edgeList, 0, tmp, 0, this.index);
        return tmp;
    }
}
