package summer_practice_2020.purple;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import summer_practice_2020.purple.graphgen.GraphGeneratorFacade;
import summer_practice_2020.purple.rendering.Edge;
import summer_practice_2020.purple.rendering.Node;
import summer_practice_2020.purple.rendering.Renderer;
import summer_practice_2020.purple.rendering.WorkStep;

public class Controller {
    Graph graphToWork;
    Boruvka algorithm;
    Renderer renderer;
    List<WorkStep> stepList;
    int index;

    boolean isGraphBlocked;
    boolean nodeMoveMode;
    boolean addEdgeMode;

    Node selectedNode;
    Node nodeForEdge;
    Edge selectedEdge;

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

        next.setDisable(true);
        previous.setDisable(true);
        stop.setDisable(true);


        this.isGraphBlocked = false;
        this.nodeMoveMode = false;
        this.addEdgeMode = false;
        this.graphToWork = new Graph();
        this.canvas.widthProperty().bind(canvas_container.widthProperty());
        this.canvas.heightProperty().bind(canvas_container.heightProperty());
        this.renderer = new Renderer(this.canvas);


        this.renderer.setGraph(this.graphToWork);
        //this.renderer.setEdgeSet(this.algorithm.next());


        generateGraph.setOnAction(e -> this.generateGraph());

        play_pause.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.isGraphBlocked = true;
            this.algorithm = new Boruvka(this.graphToWork);
            this.algorithm.boruvka();
            this.stepList = new LinkedList<>();
            this.index = 0;

            stop.setDisable(false);
            if (this.algorithm.hasNext()) {
                next.setDisable(false);
            }
        });

        next.setOnMouseClicked(e -> {
            if (this.algorithm.hasNext()) {
                this.stepList.add(new WorkStep(this.algorithm.next(), this.graphToWork));
                previous.setDisable(false);
                this.renderer.addToEdgeSet(this.stepList.get(this.index).getEdge());
                this.list.getItems().add(this.stepList.get(this.index).getDescription());
                this.index += 1;
                this.renderer.drawGraph();
            } else {
                next.setDisable(true);
            }
        });

        stop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.renderer.setEdgeSet(null);
            this.renderer.clear();
            this.renderer.drawGraph();
            list.setItems(FXCollections.observableArrayList());
            this.isGraphBlocked = false;
        });

        canvas_container.setOnMouseMoved(e -> this.selectedNode = renderer.isNodePosition(e.getX(), e.getY()));

        canvas_container.setOnMouseDragged(e -> {
            if (this.selectedNode != null) {
                this.nodeMoveMode = true;
                this.selectedNode.getNode().setPosX(e.getX());
                this.selectedNode.getNode().setPosY(e.getY());
                this.renderer.drawGraph();
            }
        });

        canvas_container.setOnMouseDragReleased(e -> this.nodeMoveMode = false);

        canvas_container.setOnMouseClicked(e -> {
            ContextMenu menu = new ContextMenu();
            MenuItem deleteNode = new MenuItem("Удалить вершину");
            MenuItem addEdge = new MenuItem("Добавить ребро");
            MenuItem rename = new MenuItem("Переименовать вершину");
            TextArea newName = new TextArea();

            menu.getItems().addAll(deleteNode, addEdge, rename);
            newName.getOnKeyPressed();

            this.selectedNode = renderer.isNodePosition(e.getX(), e.getY());

            deleteNode.setOnAction(g -> {
                this.graphToWork.removeNode(this.selectedNode.getNode());
                this.renderer.drawGraph();
            });

            addEdge.setOnAction(g -> {
                this.nodeForEdge = this.selectedNode;
                this.addEdgeMode = true;
            });

            rename.setOnAction(g -> this.editPole(e));

            if (e.getButton() == MouseButton.PRIMARY) {
                if (!addEdgeMode) {
                    if (this.selectedNode == null) {
                        this.selectedEdge = this.renderer.isEdgePosition(e.getX(), e.getY());
                        if (this.selectedEdge != null) {
                            this.editPole(e);
                        } else {
                            IGraph.Node addedNode = this.graphToWork.addNode();
                            addedNode.setTitle("name");
                            addedNode.setPosX(e.getX());
                            addedNode.setPosY(e.getY());
                            renderer.drawGraph();
                        }
                    }
                } else if (this.selectedNode != null) {
                    if (!this.selectedNode.equals(this.nodeForEdge)) {
                        IGraph.Edge edge = this.graphToWork.addEdge(this.selectedNode.getNode(), this.nodeForEdge.getNode());
                        edge.setWeight(0);
                        this.addEdgeMode = false;
                        this.renderer.drawGraph();
                    }
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                if (this.selectedNode != null) {
                    menu.show(canvas_container, e.getScreenX(), e.getScreenY());
                } else {
                    this.selectedEdge = this.renderer.isEdgePosition(e.getX(), e.getY());
                    if (this.selectedEdge != null) {
                        this.graphToWork.removeEdge(this.selectedEdge.getEdge());
                        this.renderer.drawGraph();
                    }
                }

            }
        });
    }

    private void editPole(MouseEvent e) {
        Stage editStage = new Stage();
        Pane root = new Pane();
        TextField textField = this.selectedNode == null ?
                new TextField(Long.toString(Math.round(this.selectedEdge.getWeight())))
                : new TextField(this.selectedNode.getTitle());
        textField.selectAll();
        root.getChildren().addAll(textField);
        Scene editScene = new Scene(root);
        editStage.setScene(editScene);
        editStage.setX(e.getScreenX());
        editStage.setY(e.getScreenY());
        editStage.setAlwaysOnTop(true);
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.initStyle(StageStyle.UNDECORATED);
        editStage.show();

        textField.setOnKeyPressed(f -> {
            if (f.getCode() == KeyCode.ENTER) {
                if (this.selectedNode != null) {
                    this.selectedNode.getNode().setTitle(textField.getText());
                } else {
                    this.selectedEdge.getEdge().setWeight(Double.parseDouble(textField.getText()));
                }
                this.renderer.drawGraph();
                editStage.close();
            }
        });
    }


    @FXML
    private void generateGraph() {
        Stage dialog = new Stage();
        dialog.setTitle("Параметры генерации");
        VBox root = new VBox();

        TextField nodesCount = new TextField();
        nodesCount.setPromptText("Введите количество верщин");
        CheckBox connected = new CheckBox();
        connected.setAllowIndeterminate(false);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        gridPane.add(new Label("Количество вершин:"), 0, 0);
        gridPane.add(nodesCount, 1, 0);
        gridPane.add(new Label("Соединить вершины:"), 0, 1);
        gridPane.add(connected, 1, 1);

        Button submit = new Button("Принять");
        submit.setDefaultButton(true);

        root.getChildren().addAll(gridPane, submit);

        Scene scene = new Scene(root);

        dialog.setScene(scene);

        dialog.show();

        submit.setOnAction(e -> {
            this.graphToWork = new Graph();
            this.renderer.setGraph(this.graphToWork);
            new GraphGeneratorFacade().generateGraph(this.graphToWork,
                    Integer.parseInt(nodesCount.getText()), connected.isSelected());
            this.renderer.drawGraph();
            dialog.close();
        });
    }

}
