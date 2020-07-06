package summer_practice_2020.purple;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import summer_practice_2020.purple.rendering.Renderer;

public class Controller {
    Graph graphToWork;
    boolean isGraphBlocked;

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
        Renderer renderer = new Renderer(this.canvas);
        isGraphBlocked = false;

        generateGraph.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> new GraphGeneratorFacade().generateGraph(this.graphToWork, 5, true));

        play_pause.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.isGraphBlocked = true;
            Boruvka boruvka = new Boruvka(this.graphToWork);
            boruvka.boruvka();
        });

        stop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            renderer.clear();
            list.setItems(FXCollections.observableArrayList());
            this.isGraphBlocked = false;
        });
    }


}
