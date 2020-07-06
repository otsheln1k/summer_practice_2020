package summer_practice_2020.purple.rendering;

import summer_practice_2020.purple.IGraph;

public class Node {
    private IGraph.Node node;
    private double posx;
    private double posy;

    public Node(IGraph.Node node, double posx, double posy) {
        this.node = node;
        this.posx = posx;
        this.posy = posy;
    }

    public String getTitle(){
        return this.node.getTitle();
    }

    public IGraph.Node getNode(){
        return this.node;
    }

    public void setPosx(double x) {
        this.posx = x;
    }

    public void setPosy(double y) {
        this.posy = y;
    }

    public double getPosx() {
        return this.posx;
    }

    public double getPosy() {
        return this.posy;
    }
}
