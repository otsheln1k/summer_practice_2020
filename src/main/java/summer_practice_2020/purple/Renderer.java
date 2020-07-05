package summer_practice_2020.purple;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
    Canvas workingCanvas;
    GraphicsContext graphicsContext;

    public Renderer(Canvas canvas) {
        this.workingCanvas = canvas;
        this.graphicsContext = this.workingCanvas.getGraphicsContext2D();
    }

    public interface Draw {
        void draw();
    }

    /*
    public void drawVertex(Graph.Vertex vertex) {
        this.graphicsContext.fillOval(vertex.posx, vertex.posy, (vertex.name.length() + 2) * 12, (vertex.name.length() + 2) * 12);
        this.graphicsContext.fillText(vertex.name, vertex.posx + 12, vertex.posy / 2 + 6);
    }

    public void drawEdge(Graph.Vertex vertex1, Graph.Vertex vertex2) {
        this.graphicsContext.moveTo(vertex1.posx + (vertex1.name.length() + 2) * 6, vertex1.posy + (vertex1.name.length() + 2) * 6);
        this.graphicsContext.lineTo(vertex2.posx + (vertex2.name.length() + 2) * 6, vertex2.posy + (vertex2.name.length() + 2) * 6);
    }

    public void drawGraph(Graph graph){

    }*/
}
