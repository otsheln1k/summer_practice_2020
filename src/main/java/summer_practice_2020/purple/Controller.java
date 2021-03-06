package summer_practice_2020.purple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import summer_practice_2020.purple.boruvka.Boruvka;
import summer_practice_2020.purple.boruvka.BoruvkaSnapshot;
import summer_practice_2020.purple.graphgen.GraphGeneratorFacade;
import summer_practice_2020.purple.rendering.Edge;
import summer_practice_2020.purple.rendering.Node;
import summer_practice_2020.purple.rendering.Renderer;

public class Controller {
    Graph graphToWork;
    Boruvka algorithm;
    Renderer renderer;
    LinkedList<BoruvkaSnapshot> snapshots;
    int index;

    boolean isGraphBlocked;
    boolean nodeMoveMode;
    boolean autoShow;
    boolean addEdgeMode;
    boolean isWorkFinished;

    Node selectedNode;
    Node nodeForEdge;
    Edge selectedEdge;

    @FXML
    private MenuItem generateGraph;
    @FXML
    private MenuItem help;
    @FXML
    private MenuItem importGraph;
    @FXML
    private MenuItem about;
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
    private Button finish;

    @FXML
    private void initialize() {

        this.next.setDisable(true);
        this.previous.setDisable(true);
        this.stop.setDisable(true);
        this.finish.setDisable(true);


        this.isGraphBlocked = false;
        this.nodeMoveMode = false;
        this.addEdgeMode = false;
        this.autoShow = false;

        this.graphToWork = new Graph();
        this.canvas.widthProperty().bind(canvas_container.widthProperty());
        this.canvas.heightProperty().bind(canvas_container.heightProperty());
        this.renderer = new Renderer(this.canvas);


        this.renderer.setGraph(this.graphToWork);

        this.generateGraph.setOnAction(e -> this.generateGraph());

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

        this.importGraph.setOnAction(e -> {
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

        this.exportGraph.setOnAction(e -> {
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

        this.list.setOnMouseClicked(e -> {
            this.index = this.list.getSelectionModel().getSelectedIndex();
            if (this.index < this.snapshots.size()) {
                this.renderer.setSnapshot(this.snapshots.get(this.index));
            } else {
                this.renderer.setEdgeSet(this.algorithm.resultEdgeSet());
            }
            this.renderer.drawGraph();
            if (this.index == this.snapshots.size()) {
                this.next.setDisable(true);
            } else {
                this.next.setDisable(false);
                this.previous.setDisable(this.index <= 0);
            }
        });

        this.finish.setOnMouseClicked(ae -> {
            this.finish.setDisable(true);
            this.resultShow();
        });

        this.play_pause.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (this.graphToWork.edgesCount() > 0) {
                if (!this.isGraphBlocked) {
                    this.isWorkFinished = false;
                    this.finish.setDisable(false);
                    this.isGraphBlocked = true;
                    this.importGraph.setDisable(true);
                    this.generateGraph.setDisable(true);
                    this.algorithm = new Boruvka(this.graphToWork);
                    this.algorithm.boruvka();
                    this.index = -1;
                    this.snapshots = new LinkedList<>();

                    while (this.algorithm.hasNext()) {
                        this.snapshots.add(this.algorithm.next());
                    }
                    stop.setDisable(false);

                    if (this.index < this.snapshots.size()) {
                        this.next.setDisable(false);
                    }
                }
            }
        });

        this.next.setOnAction(e -> {
            this.index += 1;
            if (this.index < this.snapshots.size()) {
                if (this.list.getItems().size() <= this.index) {
                    this.list.getItems().add("Выбрано ребро между " +
                            this.snapshots.get(index).getSelectedEdge().firstNode().getTitle()
                            + " и " + this.snapshots.get(index).getSelectedEdge().secondNode().getTitle());
                }
                this.list.getSelectionModel().select(index);
                this.renderer.setSnapshot(this.snapshots.get(this.list.getSelectionModel().getSelectedIndex()));
                this.renderer.drawGraph();
            }
            if (this.index == this.snapshots.size()) {
                if (this.list.getItems().size() == this.snapshots.size()) {
                    this.list.getItems().add("Конец работы алгоритма");
                }
                this.list.getSelectionModel().select(index);
                this.renderer.setEdgeSet(this.algorithm.resultEdgeSet());
                this.renderer.drawGraph();
                next.setDisable(true);
            } else previous.setDisable(this.index <= 0);
        });


        this.previous.setOnMouseClicked(e -> {
            this.index -= 1;
            this.next.setDisable(false);
            this.list.getSelectionModel().select(index);
            this.renderer.setSnapshot(this.snapshots.get(this.list.getSelectionModel().getSelectedIndex()));
            this.renderer.drawGraph();
            if (this.index < 1) {
                this.previous.setDisable(true);
            }
        });

        this.stop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.renderer.setSnapshot(null);
            this.renderer.clear();
            this.renderer.drawGraph();
            this.list.setItems(FXCollections.observableArrayList());
            this.isGraphBlocked = false;
            this.importGraph.setDisable(false);
            this.generateGraph.setDisable(false);
            this.next.setDisable(true);
            this.previous.setDisable(true);
            this.finish.setDisable(true);
        });

        this.canvas_container.setOnMouseMoved(e -> this.selectedNode = renderer.isNodePosition(e.getX(), e.getY()));

        this.canvas_container.setOnMouseDragged(e -> {
            if (this.selectedNode != null) {
                this.nodeMoveMode = true;
                this.selectedNode.getNode().setPosX(e.getX());
                this.selectedNode.getNode().setPosY(e.getY());
                this.renderer.drawGraph();
            }
        });

        this.canvas_container.setOnMouseDragReleased(e -> this.nodeMoveMode = false);

        this.canvas_container.setOnMouseClicked(e -> {
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

            rename.setOnAction(g -> {
            	this.editPole(e);
            });

            if (e.getButton() == MouseButton.PRIMARY && !this.isGraphBlocked) {
                if (!addEdgeMode) {
                    if (this.selectedNode == null) {
                        this.selectedEdge = this.renderer.isEdgePosition(e.getX(), e.getY());
                        if (this.selectedEdge != null) {
                            this.editPole(e);
                        } else {
                            IGraph.Node addedNode = this.graphToWork.addNode();
                            addedNode.setTitle("Имя");
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
                new TextField(Double.toString(this.selectedEdge.getWeight()))
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

    private void resultShow() {
        this.isWorkFinished = true;
        while (this.index < this.snapshots.size()){
            this.next.fire();
        }
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

    @FXML
    private void help(){

        Stage dialog = new Stage();
        dialog.setTitle("Помощь");
        dialog.setWidth(750);
        dialog.setHeight(600);
        VBox root = new VBox();

        Text text = new Text();
        text.setText("\tАлгоритм Борyвки — это алгоритм нахождения минимального остовного дерева в графе. Работа алгоритма состоит из нескольких итераций, каждая из которых состоит в последовательном добавлении рёбер к остовному лесу графа, до тех пор, пока лес не превратится в дерево, то есть, лес, состоящий из одной компоненты связности. " +
                "\n\nУправление\n" +
                "   Для добавления вершины нажмите левой кнопкой мыши на свободном месте.\n" +
                "   Для переместить вершину зажмите левую кнопку мыши на вершине и переместите куда требуется\n" +
                "   Для удаления вершины или ее переименования нажмите на вершине правой кнопкой мыши и выберете соответствующий пункт в появившемся меню\n" +
                "   Для добавления ребра нажмите правой кнопкой мыши на вершину, из которой хотите пустить ребро, и выберете соответствующий пункт меню, затем нажмите левой кнопкой мыши на вершине, в которую будет вести ребро\n" +
                "   Для удаления ребра нажмите правой кнопкой мыши на вес ребра\n" +
                "   Для изменения веса ребра нажмите левой кнопкой мыши на текущей вес ребра\n\n" +
                "   Для запуска алгоритма нажмите на кнопку \"▸\"\n" +
                "   Для отображения следующего шага алгоритма нажмите кнопку \"»\"\n" +
                "   Для отображения предыдущего шага  алгоритма нажмите кнопку \"«\"\n" +
                "   Для остановки алгоритма нажмите кнопку \"▪\"\n" +
                "   Для отображения полученого каркаса нажмите кнопку \"Результат работы\"");
        text.setWrappingWidth (740);
        text.setFont(Font.font("Arial", 16));

        root.getChildren().addAll(text);

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.show();
    }

    @FXML
    private void about(){
        Stage dialog = new Stage();
        VBox root = new VBox();
        Text text = new Text();
        text.setText("\n Программа визуализирует работу алгоритма Борувки (алгоритма построения минимального остовного дерева)\n" +
                " Создатели:\n\tАбибулаев Эльдар\n\tПарфентьев Леонид\n\tРудько Даниил\n\n");
        text.setWrappingWidth (500);
        root.getChildren().add(text);
        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.show();

    }
}
