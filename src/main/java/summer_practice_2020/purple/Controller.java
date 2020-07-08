package summer_practice_2020.purple;

import java.util.Set;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import summer_practice_2020.purple.graphgen.GraphGeneratorFacade;
import summer_practice_2020.purple.rendering.Node;
import summer_practice_2020.purple.rendering.Renderer;

public class Controller {
    Graph graphToWork;
    boolean isGraphBlocked;
    Renderer renderer;
    boolean isLeftMouseDown;
    Node selectedNode;

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
        this.isGraphBlocked = false;
        this.graphToWork = new Graph();
        this.renderer = new Renderer(this.canvas);
        renderer.setGraph(this.graphToWork);


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
            this.renderer.setEdgeSet(null);
            this.renderer.clear();
            this.renderer.drawGraph();
            list.setItems(FXCollections.observableArrayList());
            this.isGraphBlocked = false;
        });

        canvas_container.setOnMouseMoved(e -> {
            System.out.println("Mouse move detected at x = " + e.getX() + " y = " + e.getY());
            if (this.isLeftMouseDown && this.selectedNode != null) {
                this.selectedNode.setPosx(e.getX());
                this.selectedNode.setPosy(e.getY());
                this.renderer.drawGraph();
            }
        });

        canvas_container.setOnMouseClicked(e -> {
            ContextMenu menu = new ContextMenu();
            MenuItem rename = new MenuItem("Переименовать вершину");
            MenuItem deleteNode = new MenuItem("Удалить вершину");
            MenuItem deleteEdge = new MenuItem("Удалить ребро");
            MenuItem[] edgeList = new MenuItem[0];
            menu.getItems().addAll(rename, deleteNode, deleteEdge);

            this.selectedNode = renderer.isNodePosition(e.getX(), e.getY());

            deleteNode.setOnAction(g -> {
                this.graphToWork.removeNode(this.selectedNode.getNode());
                this.renderer.drawGraph();
            });

            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.println("Primary at x = " + e.getX() + " y = " + e.getY());
                if (this.selectedNode == null) {
                    IGraph.Node addedNode = this.graphToWork.addNode();
                    addedNode.setTitle("name");
                    addedNode.setPosX(e.getX());
                    addedNode.setPosY(e.getY());
                    renderer.drawGraph();
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                System.out.println("Secondary at x = " + e.getX() + " y = " + e.getY());
                if (renderer.isNodePosition(e.getX(), e.getY()) != null) {
                    menu.show(canvas_container, e.getScreenX(), e.getScreenY());
                }

            }
        });
    }

    @FXML
    private void generateGraph() {
        new GraphGeneratorFacade().generateGraph(this.graphToWork, 5, true);
        this.renderer.drawGraph();
        System.out.println("generated");
    }

}
