package summer_practice_2020.purple;

import java.util.Set;

import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import summer_practice_2020.purple.graphgen.GraphGeneratorFacade;
import summer_practice_2020.purple.rendering.Node;
import summer_practice_2020.purple.rendering.Renderer;

public class Controller {
    Graph graphToWork;
    Renderer renderer;
    boolean isGraphBlocked;
    boolean nodeMoveMode;
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
        this.nodeMoveMode = false;
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
            if (this.selectedNode != null && this.nodeMoveMode) {
                System.out.println("Node move detected at x = " + e.getX() + " y = " + e.getY());
                this.selectedNode.getNode().setPosX(e.getX());
                this.selectedNode.getNode().setPosY(e.getY());
                this.renderer.drawGraph();
            }
        });

        canvas_container.setOnMouseClicked(e -> {
            ContextMenu menu = new ContextMenu();
            MenuItem rename = new MenuItem("Переименовать вершину");
            MenuItem deleteNode = new MenuItem("Удалить вершину");
            MenuItem deleteEdge = new MenuItem("Удалить ребро");
            MenuItem[] edgeList = new MenuItem[0];
            TextArea newName = new TextArea();

            menu.getItems().addAll(rename, deleteNode, deleteEdge);
            newName.getOnKeyPressed();

            this.selectedNode = renderer.isNodePosition(e.getX(), e.getY());

            deleteNode.setOnAction(g -> {
                this.graphToWork.removeNode(this.selectedNode.getNode());
                this.renderer.drawGraph();
            });

            rename.setOnAction(g -> {
                Stage nameEditStage = new Stage();
                Pane root = new Pane();
                TextField textField = new TextField();
                root.getChildren().addAll(textField);
                Scene nameEditScene = new Scene(root);
                nameEditStage.setScene(nameEditScene);
                nameEditStage.setX(e.getScreenX());
                nameEditStage.setY(e.getScreenY());
                nameEditStage.setAlwaysOnTop(true);
                nameEditStage.initModality(Modality.APPLICATION_MODAL);
                nameEditStage.initStyle(StageStyle.UNDECORATED);
                nameEditStage.show();

                textField.setOnKeyPressed(f -> {
                    if (f.getCode() == KeyCode.ENTER) {
                        this.selectedNode.getNode().setTitle(textField.getText());
                        this.renderer.drawGraph();
                        nameEditStage.close();
                    }
                });
            });

            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.println("Primary at x = " + e.getX() + " y = " + e.getY());
                if (this.selectedNode == null) {
                    IGraph.Node addedNode = this.graphToWork.addNode();
                    addedNode.setTitle("name");
                    addedNode.setPosX(e.getX());
                    addedNode.setPosY(e.getY());
                    renderer.drawGraph();
                } else {
                    this.nodeMoveMode = true;
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                System.out.println("Secondary at x = " + e.getX() + " y = " + e.getY());
                if (renderer.isNodePosition(e.getX(), e.getY()) != null) {
                    if (this.nodeMoveMode) {
                        this.nodeMoveMode = false;
                    } else {
                        menu.show(canvas_container, e.getScreenX(), e.getScreenY());
                    }
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
