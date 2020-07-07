package summer_practice_2020.purple.rendering;

import javafx.scene.paint.Color;
import summer_practice_2020.purple.IGraph;

public class Node {
    private IGraph.Node node;
    private double posx;
    private double posy;
    private double radius;
    private Color color;

    public Node(IGraph.Node node, double posx, double posy, Color color) {
        this.node = node;
        this.posx = posx;
        this.posy = posy;
        this.color = color;
        this.radius = (node.getTitle().length() + 2) * 6;
    }

    public String getTitle() {
        return this.node.getTitle();
    }

    public IGraph.Node getNode() {
        return this.node;
    }

    public void setPosx(double x) {
        this.posx = x;
    }

    public void setPosy(double y) {
        this.posy = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getPosx() {
        return this.posx;
    }

    public double getPosy() {
        return this.posy;
    }

    public double getRadius() { return this.radius; }

    public Color getColor() {
        return this.color;
    }
}
