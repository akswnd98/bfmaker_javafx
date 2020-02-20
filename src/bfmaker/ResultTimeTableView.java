package bfmaker;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

import java.util.ListIterator;

public class ResultTimeTableView extends GridPane {
    public final int MAX_DAY;
    public final int MAX_TIME;
    private Label[][] labels;

    public void set(int day, int time, String text) {
        labels[time][day].setText(text);
    }

    public void clearAll() {
        for(int i = 0; i < MAX_TIME; i++) {
            for(int j = 0; j < MAX_DAY; j++) {
                set(j, i, null);
            }
        }
    }

    public void setTable(OutputPlan.Table table) {
        clearAll();
        ListIterator<OutputPlan.Table.Course> it = table.getCourses().listIterator();
        while(it.hasNext()) {
            OutputPlan.Table.Course course = it.next();
            ListIterator<Cord> it2 = course.getCourseTimes().listIterator();
            while(it2.hasNext()) {
                Cord cord = it2.next();
                set(cord.getDay(), cord.getTime(), course.getSubjName() + "\n" + course.getProfessor());
            }
        }
    }

    public ResultTimeTableView(int MAX_DAY, int MAX_TIME) {
        super();
        setGridLinesVisible(true);
        this.MAX_DAY = MAX_DAY;
        this.MAX_TIME = MAX_TIME;
        labels = new Label[MAX_TIME][MAX_DAY];
        int i, j;
        for(i = 0; i < MAX_TIME; i++) {
            for(j = 0; j < MAX_DAY; j++) {
                labels[i][j] = new Label(null);
                labels[i][j].setAlignment(Pos.CENTER);
                Pane pane = new Pane();
                pane.getChildren().add(labels[i][j]);
                pane.setPrefHeight(50);
                pane.setMinHeight(50);
                pane.setMaxHeight(50);
                add(pane, j + 1, i + 1);
                setHgrow(pane, Priority.ALWAYS);
                setVgrow(pane, Priority.NEVER);
            }
        }
        String[] ord = {"st", "nd", "rd"};
        for(i = 1; i <= MAX_DAY; i++) {
            Label label = new Label(Integer.toString(i) + ((i <= 3) ? ord[i - 1] : "th") + " day");
            Pane pane = new Pane();
            pane.getChildren().add(label);
            pane.setPrefHeight(50);
            pane.setMinHeight(50);
            pane.setMaxHeight(50);
            label.setAlignment(Pos.CENTER);
            add(pane, i, 0);
        }
        for(i = 1; i <= MAX_TIME; i++) {
            Label label = new Label(Integer.toString(i) + ((i <= 3) ? ord[i - 1] : "th") + " class");
            Pane pane = new Pane();
            pane.getChildren().add(label);
            pane.setPrefHeight(50);
            pane.setMinHeight(50);
            pane.setMaxHeight(50);
            label.setAlignment(Pos.CENTER);
            add(pane, 0, i);
        }
    }
}
