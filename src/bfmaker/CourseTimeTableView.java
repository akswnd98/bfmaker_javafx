package bfmaker;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class CourseTimeTableView extends GridPane {
    public static class CellPane extends Pane {
        private int day;
        private int time;
        private CourseTimeTableView courseTimeTableView;
        private Bfmaker bfmaker;
        private MainController mainController;
        public CellPane(int day, int time, CourseTimeTableView courseTimeTableView) {
            super();
            this.day = day;
            this.time = time;
            this.courseTimeTableView = courseTimeTableView;
            this.bfmaker = courseTimeTableView.bfmaker;
            this.mainController = bfmaker.getMainController();
            setOnMouseClicked(event -> {
                if(this.courseTimeTableView.isSelected(this.day, this.time)) {
                    this.courseTimeTableView.setSelected(this.day, this.time, false);
                    int subjIdx, courseIdx;
                    if(this.mainController.getSubjListView().getSelectionModel().isEmpty()) {
                        return;
                    }
                    subjIdx = this.mainController.getSubjListView().getSelectionModel().getSelectedIndex();
                    if(mainController.getCourseListView().getSelectionModel().isEmpty()) {
                        return;
                    }
                    courseIdx = this.mainController.getCourseListView().getSelectionModel().getSelectedIndex();
                    this.bfmaker.removeCourseTime(subjIdx, courseIdx, new Cord(this.day, this.time));
                } else {
                    this.courseTimeTableView.setSelected(this.day, this.time, true);
                    int subjIdx, courseIdx;
                    if(this.mainController.getSubjListView().getSelectionModel().isEmpty()) {
                        return;
                    }
                    subjIdx = this.mainController.getSubjListView().getSelectionModel().getSelectedIndex();
                    if(this.mainController.getCourseListView().getSelectionModel().isEmpty()) {
                        return;
                    }
                    courseIdx = this.mainController.getCourseListView().getSelectionModel().getSelectedIndex();
                    this.bfmaker.addCourseTime(subjIdx, courseIdx, new Cord(this.day, this.time));
                }
            });
            setStyle("-fx-background-color: transparent;");
        }
    }
    public static class EdgePane extends Pane {
        private Label label;
        public Label getLabel() {
            return label;
        }
        public void setLabel(Label label) {
            this.label = label;
        }
        public EdgePane() {
            label = new Label();
            label.setAlignment(Pos.CENTER);
            getChildren().add(label);
            setPrefHeight(50);
            setMinHeight(50);
            setMaxHeight(50);
        }
        public EdgePane(String text) {
            label = new Label(text);
            label.setAlignment(Pos.CENTER);
            getChildren().add(label);
            setPrefHeight(50);
            setMinHeight(50);
            setMaxHeight(50);
        }
    }
    public final int MAX_DAY;
    public final int MAX_TIME;
    private Bfmaker bfmaker;
    private boolean[][] selected;
    public boolean isSelected(int day, int time) {
        return selected[time][day];
    }
    public void setSelected(int day, int time, boolean sel) {
        selected[time][day] = sel;
        if(sel) {
            cellPanes[time][day].setStyle("-fx-background-color: gray;");
        } else {
            cellPanes[time][day].setStyle("-fx-background-color: transparent;");
        }
    }
    private CellPane[][] cellPanes;
    public void deselectAll() {
        int i, j;
        for(i = 0; i < MAX_TIME; i++) {
            for(j = 0; j < MAX_DAY; j++) {
                setSelected(j, i, false);
            }
        }
    }

    public CourseTimeTableView(int MAX_DAY, int MAX_TIME, Bfmaker bfmaker) {
        super();
        this.bfmaker = bfmaker;
        setGridLinesVisible(true);
        int i, j;
        this.MAX_DAY = MAX_DAY;
        this.MAX_TIME = MAX_TIME;
        selected = new boolean[MAX_TIME][MAX_DAY];
        cellPanes = new CellPane[MAX_TIME][MAX_DAY];
        for(i = 0; i < MAX_TIME; i++) {
            for(j = 0; j < MAX_DAY; j++) {
                cellPanes[i][j] = new CellPane(j, i, this);
                cellPanes[i][j].setPrefHeight(50);
                cellPanes[i][j].setMinHeight(50);
                cellPanes[i][j].setMaxHeight(50);
                add(cellPanes[i][j], j + 1, i + 1);
                setHgrow(cellPanes[i][j], Priority.ALWAYS);
                setVgrow(cellPanes[i][j], Priority.NEVER);
                setSelected(j, i, false);
            }
        }
        String[] ord = {"st", "nd", "rd"};
        for(i = 1; i <= MAX_DAY; i++) {
            add(new EdgePane(Integer.toString(i) + ((i <= 3) ? ord[i - 1] : "th") + " day"), i, 0);
        }
        for(i = 1; i <= MAX_TIME; i++) {
            add(new EdgePane(Integer.toString(i) + ((i <= 3) ? ord[i - 1] : "th") + " class"), 0, i);
        }
    }
}
