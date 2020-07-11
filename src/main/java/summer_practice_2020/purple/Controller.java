package summer_practice_2020.purple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import summer_practice_2020.purple.boruvka.Boruvka;
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
    boolean autoShow;
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

        this.next.setDisable(true);
        this.previous.setDisable(true);
        this.stop.setDisable(true);
        this.speed_control.setMin(0);
        this.speed_control.setMax(10);
        this.speed_control.setBlockIncrement(0.5);


        this.isGraphBlocked = false;
        this.nodeMoveMode = false;
        this.addEdgeMode = false;
        this.autoShow = false;

        this.graphToWork = new Graph();
        this.canvas.widthProperty().bind(canvas_container.widthProperty());
        this.canvas.heightProperty().bind(canvas_container.heightProperty());
        this.renderer = new Renderer(this.canvas);


        this.renderer.setGraph(this.graphToWork);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), ae -> {
                    System.out.println("TimerClock " + ((1000 * 10) - this.speed_control.getValue() * 1000));
                    this.next.fire();
                }));

        timeline.setCycleCount(99999);


        generateGraph.setOnAction(e -> this.generateGraph());

        this.list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setMinWidth(param.getWidth());
                    setMaxWidth(param.getWidth());
                    setPrefWidth(param.getWidth());
                    setWrapText(true);
                    setText(item);
                }
            }
        });

        importGraph.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите файл графа");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file == null) return;
            try {
                FileInputStream stream = new FileInputStream(file);
                this.graphToWork = new Graph();
                GraphIO.readGraph(stream, this.graphToWork);
            } catch (FileNotFoundException fileNotFoundException) {
                Alert importError = new Alert(Alert.AlertType.ERROR);
                importError.setContentText("Ошибка при импортировании графа");
                importError.showAndWait();
            }
            this.renderer.setGraph(this.graphToWork);
            this.renderer.drawGraph();
        });

        exportGraph.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите директорию для сохранения графа");
            fileChooser.setInitialFileName("Graph");
            File newGraphFile = fileChooser.showSaveDialog(new Stage());
            if (newGraphFile == null) return;
            try {
                FileOutputStream stream = new FileOutputStream(newGraphFile);
                GraphIO.writeGraph(stream, this.graphToWork);
            } catch (FileNotFoundException fileNotFoundException) {
                Alert importError = new Alert(Alert.AlertType.ERROR);
                importError.setContentText("Ошибка при экспортировании графа");
                importError.showAndWait();
            }
        });

        speed_control.setOnTouchReleased(e -> timeline.setRate((1000 * 10) - this.speed_control.getValue() * 1000));

        play_pause.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (timeline.getStatus() == Animation.Status.STOPPED) {
                this.isGraphBlocked = true;
                this.renderer.beginVisualization();
//                this.renderer.setEdgeSet(new HashSet<>());
                this.algorithm = new Boruvka(this.graphToWork);
                this.algorithm.boruvka();
                this.stepList = new LinkedList<>();
                this.index = 0;

                stop.setDisable(false);

                if (this.algorithm.hasNext()) {
                    next.setDisable(false);
                    timeline.setRate((1000 * 10) - this.speed_control.getValue() * 1000);
                    timeline.play();
                }
            } else {
                timeline.pause();
            }
        });

        next.setOnMouseClicked(e -> {
            if (this.algorithm.hasNext()) {
                WorkStep ws = new WorkStep(this.algorithm.next());
                this.stepList.add(ws);
                previous.setDisable(false);
                
                // TODO: remove and set the full edge set/predicate instead
                this.renderer.addToEdgeSet(ws.getEdge());
                
                // TODO: set a whole WorkStep (or its view) or Snapshot?
                this.renderer.setLastEdge(ws.getEdge());
                this.renderer.setGroups(ws.getGroups());
                this.renderer.setAvailEdgePredicate(
                		ws.getAvailableEdgePredicate());
                this.renderer.setMergedToGroup(ws.getMergedToGroup());
                this.renderer.setMergedGroup(ws.getMergedGroup());
                
                this.list.getItems().add(ws.getDescription());
                this.index += 1;
                this.renderer.drawGraph();
            } else {
            	this.renderer.lastStep();
                this.renderer.drawGraph();

                this.list.getItems().add("Конец работы алгоритма");
                next.setDisable(true);
                timeline.stop();
            }
        });


        previous.setOnMouseClicked(e -> {

        });

        stop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.renderer.endVisualization();
            this.renderer.drawGraph();
            list.setItems(FXCollections.observableArrayList());
            this.isGraphBlocked = false;
            this.stop.setDisable(true);
            this.next.setDisable(true);
            this.previous.setDisable(true);
            timeline.stop();
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

            if (e.getButton() == MouseButton.PRIMARY && !this.isGraphBlocked) {
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
