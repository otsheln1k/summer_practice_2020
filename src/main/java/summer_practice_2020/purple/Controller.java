package summer_practice_2020.purple;

import java.util.Set;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import summer_practice_2020.purple.graphgen.GraphGeneratorFacade;
import summer_practice_2020.purple.rendering.Renderer;

public class Controller {
    Graph graphToWork;
    boolean isGraphBlocked;
    Renderer renderer;

    @FXML
    private MenuItem generateGraph;
    @FXML
    private MenuItem importGraph;
    @FXML
    private MenuItem exportGraph;
    @FXML
    private Pane canvas_container;
    @FXML
    private Button previous;
    @FXML
    private Button play_pause;
    @FXML
    private Button stop;
    @FXML
    private Button next;
    @FXML
    private ListView<String> list;
    @FXML
    private Canvas canvas;
    @FXML
    private Slider speed_control;

    @FXML
    private void initialize() {
        this.renderer = new Renderer(this.canvas);
        this.isGraphBlocked = false;

        generateGraph.setOnAction(e -> this.generateGraph());

        play_pause.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.isGraphBlocked = true;
            Boruvka boruvka = new Boruvka(this.graphToWork);
            boruvka.boruvka();
            Set<IGraph.Edge> edges = boruvka.resultEdgeSet();
            this.renderer.setEdgeSet(edges);
            this.renderer.clear();
            this.renderer.drawGraph();
        });

        stop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.renderer.clear();
            list.setItems(FXCollections.observableArrayList());
            this.isGraphBlocked = false;
        });
    }

    @FXML
    private void generateGraph(){
        this.graphToWork = new Graph();
        new GraphGeneratorFacade().generateGraph(this.graphToWork, 5, true);
        this.renderer.setGraph(this.graphToWork);
        this.renderer.drawGraph();
        System.out.println("generated");
    }

}
