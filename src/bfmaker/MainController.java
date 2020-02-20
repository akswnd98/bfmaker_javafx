package bfmaker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.ListIterator;

public class MainController {
    private Bfmaker bfmaker;
    public void setBfmaker(Bfmaker bfmaker) {
        this.bfmaker = bfmaker;
        updateSubjListView();
        updateCourseListView();
        subjListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            courseListView.getSelectionModel().clearSelection();
            updateCourseListView();
        }));
        subjListView.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            courseListView.getSelectionModel().clearSelection();
            updateCourseListView();
        }));
        subjListView.setCellFactory(listView -> {
            return new ListCell<String>() {
                String prevValue;
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setStyle("-fx-background-color: transparent;");
                    if(empty || item.isEmpty()) {
                        setText(null);
                        return;
                    }
                    setText(item);
                    int i = getIndex();
                    if(i < 0 || i >= bfmaker.getInputPlan().getSubjs().size()) {
                        setStyle("-fx-background-color: transparent;");
                        return;
                    }
                    if(isSelected()) {
                        setStyle("-fx-background-color: -fx-focus-color;");
                    } else {
                        if (bfmaker.getInputPlan().getSubjs().get(i).isEnabled()) {
                            setStyle("-fx-background-color: transparent;");
                        } else {
                            setStyle("-fx-background-color: gray;");
                        }
                    }
                }
                @Override
                public void startEdit() {
                    super.startEdit();
                    prevValue = getText();
                    TextField textField = new TextField(getText());
                    textField.setOnAction(event -> {
                        commitEdit(textField.getText());
                    });
                    setGraphic(textField);
                    setText(null);
                }
                @Override
                public void commitEdit(String newValue) {
                    super.commitEdit(newValue);
                    setText(newValue);
                    setGraphic(null);
                }
                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setText(prevValue);
                    setGraphic(null);
                }
            };
        });
        courseListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            updateCourseTableView();
        }));
        courseListView.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            updateCourseTableView();
        }));
        courseListView.setCellFactory(listView -> {
            return new ListCell<String>() {
                String prevValue;
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setStyle("-fx-background-color: transparent;");
                    if(empty || item.isEmpty()) {
                        setText(null);
                        return;
                    }
                    setText(item);
                    int subjIdx, courseIdx;
                    if(subjListView.getSelectionModel().isEmpty()) {
                        return;
                    }
                    subjIdx = subjListView.getSelectionModel().getSelectedIndex();
                    courseIdx = getIndex();
                    if(courseIdx < 0 || courseIdx >= bfmaker.getInputPlan().getSubjs().get(subjIdx).getCourses().size()) {
                        return;
                    }
                    if(isSelected()) {
                        setStyle("-fx-background-color: -fx-focus-color;");
                    } else {
                        if(bfmaker.getInputPlan().getSubjs().get(subjIdx).getCourses().get(courseIdx).isEnabled()) {
                            setStyle("-fx-background-color: transparent;");
                        } else {
                            setStyle("-fx-background-color: gray;");
                        }
                    }
                }
                @Override
                public void startEdit() {
                    super.startEdit();
                    prevValue = getText();
                    TextField textField = new TextField(getText());
                    textField.setOnAction(event -> {
                        commitEdit(textField.getText());
                    });
                    setGraphic(textField);
                    setText(null);
                }
                @Override
                public void commitEdit(String newValue) {
                    super.commitEdit(newValue);
                    setText(newValue);
                    setGraphic(null);
                }
                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setText(prevValue);
                    setGraphic(null);
                }
            };
        });
        courseTableView = new CourseTimeTableView(bfmaker.getInputPlan().MAX_DAY, bfmaker.getInputPlan().MAX_TIME, bfmaker);
        courseScrollPane.setContent(courseTableView);
        updateCourseTableView();
        resultTimeTableView = new ResultTimeTableView(bfmaker.getOutputPlan().MAX_DAY, bfmaker.getOutputPlan().MAX_TIME);
        resultScrollPane.setContent(resultTimeTableView);
        resultTimeTableView.clearAll();
    }

    @FXML private ListView<String> subjListView;
    public ListView<String> getSubjListView() {
        return subjListView;
    }
    public void updateSubjListView() {
        subjList = FXCollections.observableArrayList();
        ListIterator<InputPlan.Subj> it = bfmaker.getInputPlan().getSubjs().listIterator();
        while(it.hasNext()) {
            subjList.add(it.next().getSubjName());
        }
        subjListView.setItems(subjList);
        subjListView.refresh();
    }
    private ObservableList<String> subjList;

    @FXML private ListView<String> courseListView;
    public ListView<String> getCourseListView() {
        return courseListView;
    }
    public void updateCourseListView() {
        courseList = FXCollections.observableArrayList();
        if(subjListView.getSelectionModel().isEmpty()) {
            courseListView.setItems(courseList);
            return;
        }
        int selectedSubjIdx = subjListView.getSelectionModel().getSelectedIndex();
        ListIterator<InputPlan.Subj.Course> it = bfmaker.getInputPlan().getSubjs().get(selectedSubjIdx).getCourses().listIterator();
        while(it.hasNext()) {
            courseList.add(it.next().getProfessor());
        }
        courseListView.setItems(courseList);
        courseListView.refresh();
    }
    private ObservableList<String> courseList;

    @FXML private ScrollPane courseScrollPane;
    private CourseTimeTableView courseTableView;
    public CourseTimeTableView getCourseTableView() {
        return courseTableView;
    }
    public void setCourseListView(ListView<String> courseListView) {
        this.courseListView = courseListView;
    }

    public void updateCourseTableView() {
        courseTableView.deselectAll();
        int subjIdx, courseIdx;
        if(!subjListView.getSelectionModel().isEmpty()) {
            subjIdx = subjListView.getSelectionModel().getSelectedIndex();
        } else {
            return;
        }
        if(!courseListView.getSelectionModel().isEmpty()) {
            courseIdx = courseListView.getSelectionModel().getSelectedIndex();
        } else {
            return;
        }
        Iterator<Cord> it = bfmaker.getInputPlan().getSubjs().get(subjIdx).getCourses().get(courseIdx).getCourseTimes().iterator();
        while(it.hasNext()) {
            Cord cord = it.next();
            courseTableView.setSelected(cord.getDay(), cord.getTime(), true);
        }
    }

    @FXML private ScrollPane resultScrollPane;
    private ResultTimeTableView resultTimeTableView;
    public ResultTimeTableView getResultTimeTableView() {
        return resultTimeTableView;
    }
    public void setCourseTableView(CourseTimeTableView courseTableView) {
        this.courseTableView = courseTableView;
    }

    private void newInputPlan(int MAX_DAY, int MAX_TIME) {

    }
    private void newOutputPlan() {
        bfmaker.newOutputPlan(bfmaker.getInputPlan().MAX_DAY, bfmaker.getInputPlan().MAX_TIME);

    }

    @FXML private void onNewAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(bfmaker.getClass().getResource("/new.fxml"));
            AnchorPane newPane = loader.load();
            NewController newController = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("new");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bfmaker.getPrimaryStage());
            dialogStage.setScene(new Scene(newPane));
            newController.setNecessary(bfmaker, dialogStage);
            dialogStage.showAndWait();
        } catch(IOException ioe) {}
    }

    @FXML private void onOpenAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(bfmaker.getPrimaryStage());
        if(file == null) {
            return;
        }
        Bfmaker newBfmaker = new Bfmaker(file);
        new Runnable() {
            @Override
            public void run() {
                newBfmaker.startNewBfmaker(newBfmaker);
            }
        }.run();
    }

    @FXML private void onSaveAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(bfmaker.getPrimaryStage());
        if(file == null) {
            return;
        }
        bfmaker.saveInputPlan(file);
    }

    @FXML private void onNewSubjAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(bfmaker.getClass().getResource("/new_subj.fxml"));
            AnchorPane newSubjPane = loader.load();
            NewSubjController newSubjController = loader.getController();
            Stage dialogStage = new Stage();
            newSubjController.setNecessary(bfmaker, dialogStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bfmaker.getPrimaryStage());
            dialogStage.setTitle("new subject");
            dialogStage.setScene(new Scene(newSubjPane));
            dialogStage.showAndWait();
        } catch(IOException ioe) {}
    }

    @FXML private void onEnableSubjAction(ActionEvent event) {
        int subjIdx;
        if(subjListView.getSelectionModel().isEmpty()) {
            return;
        }
        subjIdx = subjListView.getSelectionModel().getSelectedIndex();
        bfmaker.editSubjEnabled(subjIdx, true);
    }

    @FXML private void onDisableSubjAction(ActionEvent event) {
        int subjIdx;
        if(subjListView.getSelectionModel().isEmpty()) {
            return;
        }
        subjIdx = subjListView.getSelectionModel().getSelectedIndex();
        bfmaker.editSubjEnabled(subjIdx, false);
    }

    @FXML private void onRemoveSubjAction(ActionEvent event) {
        int subjIdx;
        if(subjListView.getSelectionModel().isEmpty()) {
            return;
        }
        subjIdx = subjListView.getSelectionModel().getSelectedIndex();
        bfmaker.removeSubj(subjIdx);
    }

    @FXML private void onNewCourseAction(ActionEvent event) {
        if(subjListView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setHeaderText("selection error");
            alert.setContentText("no subject selection");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(bfmaker.getClass().getResource("/new_course.fxml"));
            AnchorPane newCoursePane = loader.load();
            NewCourseController newCourseController = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("new course");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bfmaker.getPrimaryStage());
            dialogStage.setScene(new Scene(newCoursePane));
            newCourseController.setNecessary(bfmaker, dialogStage);
            dialogStage.showAndWait();
        } catch(IOException ioe) {}
    }

    @FXML private void onEnableCourseAction(ActionEvent event) {
        int subjIdx, courseIdx;
        if(subjListView.getSelectionModel().isEmpty()) {
            return;
        }
        subjIdx = subjListView.getSelectionModel().getSelectedIndex();
        if(courseListView.getSelectionModel().isEmpty()) {
            return;
        }
        courseIdx = courseListView.getSelectionModel().getSelectedIndex();
        bfmaker.editCourseEnabled(subjIdx, courseIdx, true);
    }

    @FXML private void onDisableCourseAction(ActionEvent event) {
        int subjIdx, courseIdx;
        if(subjListView.getSelectionModel().isEmpty()) {
            return;
        }
        subjIdx = subjListView.getSelectionModel().getSelectedIndex();
        if(courseListView.getSelectionModel().isEmpty()) {
            return;
        }
        courseIdx = courseListView.getSelectionModel().getSelectedIndex();
        bfmaker.editCourseEnabled(subjIdx, courseIdx, false);
    }

    @FXML private void onRemoveCourseAction(ActionEvent event) {
        int subjIdx, courseIdx;
        if(subjListView.getSelectionModel().isEmpty()) {
            return;
        }
        subjIdx = subjListView.getSelectionModel().getSelectedIndex();
        if(courseListView.getSelectionModel().isEmpty()) {
            return;
        }
        courseIdx = courseListView.getSelectionModel().getSelectedIndex();
        bfmaker.removeCourse(subjIdx, courseIdx);
    }

    @FXML private void onNextResultAction(ActionEvent event) {
        bfmaker.nextTableCnt();
        bfmaker.showResultTable();
    }

    @FXML private void onPrevResultAction(ActionEvent event) {
        bfmaker.prevTableCnt();
        bfmaker.showResultTable();
    }

    @FXML private void onLaunchAction(ActionEvent event) {
        bfmaker.launchAlgorithm();
    }

    @FXML private void initialize() {

    }
}
