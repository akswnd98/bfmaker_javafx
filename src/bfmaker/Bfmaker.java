package bfmaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Bfmaker extends Application {
    private Stage primaryStage;
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private VBox mainPane;
    private MainController mainController;
    public MainController getMainController() {
        return mainController;
    }
    private ObjectMapper jsonMapper;

    /*update*/
    void updateAll() {
        mainController.updateSubjListView();
        mainController.updateSubjListView();
        mainController.updateCourseListView();
    }

    /*Input Plan*/
    private InputPlan inputPlan;
    public InputPlan getInputPlan() {
        return inputPlan;
    }
    public void setInputPlan(InputPlan inputPlan) {
        this.inputPlan = inputPlan;
    }
    public void newInputPlan(int MAX_DAY, int MAX_TIME) {
        inputPlan = new InputPlan(MAX_DAY, MAX_TIME);
    }
    public void saveInputPlan(File file) {
        try {
            jsonMapper.writeValue(file, inputPlan);
        } catch(IOException ioe) {}
    }
    public void openInputPlan(File file) {
        try {
            inputPlan = jsonMapper.readValue(file, InputPlan.class);
        } catch(IOException ioe) {}
    }

    /*Output Plan*/
    private OutputPlan outputPlan;
    public OutputPlan getOutputPlan() {
        return outputPlan;
    }
    public void setOutputPlan(OutputPlan outputPlan) {
        this.outputPlan = outputPlan;
    }
    public void newOutputPlan(int MAX_DAY, int MAX_TIME) {
        outputPlan = new OutputPlan(MAX_DAY, MAX_TIME);
    }
    private int tableCnt = 0;
    public int getTableCnt() {
        return tableCnt;
    }
    public void setTableCnt(int tableCnt) {
        this.tableCnt = tableCnt;
    }
    public void nextTableCnt() {
        if(tableCnt < outputPlan.getTables().size() - 1) {
            tableCnt++;
        } else {
            tableCnt = outputPlan.getTables().size() - 1;
        }
    }
    public void prevTableCnt() {
        if(tableCnt > 0) {
            tableCnt--;
        } else {
            tableCnt = 0;
        }
    }
    public void showResultTable() {
        if(tableCnt >= outputPlan.getTables().size() || tableCnt < 0) {
            mainController.getResultTimeTableView().clearAll();
            return;
        }
        mainController.getResultTimeTableView().setTable(outputPlan.getTables().get(tableCnt));
    }

    /*Subj*/
    public void newSubj(String subjName) {
        inputPlan.getSubjs().add(new InputPlan.Subj(subjName));
        mainController.updateSubjListView();
    }
    public void editSubjName(int subjIdx, String subjName) {
        inputPlan.getSubjs().get(subjIdx).setSubjName(subjName);
        mainController.updateSubjListView();
    }
    public void editSubjEnabled(int subjIdx, boolean enabled) {
        inputPlan.getSubjs().get(subjIdx).setEnabled(enabled);
        mainController.updateSubjListView();
    }
    public void removeSubj(int subjIdx) {
        inputPlan.getSubjs().remove(subjIdx);
        mainController.updateSubjListView();
    }

    /*Course*/
    public void newCourse(int subjIdx, String professor) {
        inputPlan.getSubjs().get(subjIdx).getCourses().add(new InputPlan.Subj.Course(professor));
        mainController.updateCourseListView();
    }
    public void editProfessor(int subjIdx, int courseIdx, String professor) {
        inputPlan.getSubjs().get(subjIdx).getCourses().get(courseIdx).setProfessor(professor);
        mainController.updateCourseListView();
    }
    public void editCourseEnabled(int subjIdx, int courseIdx, boolean enabled) {
        inputPlan.getSubjs().get(subjIdx).getCourses().get(courseIdx).setEnabled(enabled);
        mainController.updateCourseListView();
    }
    public void removeCourse(int subjIdx, int courseIdx) {
        inputPlan.getSubjs().get(subjIdx).getCourses().remove(courseIdx);
        mainController.updateCourseListView();
    }
    public void addCourseTime(int subjIdx, int courseIdx, Cord courseTime) {
        ArrayList<Cord> courseTimes = inputPlan.getSubjs().get(subjIdx).getCourses().get(courseIdx).getCourseTimes();
        courseTimes.add(courseTime);
        mainController.updateCourseTableView();
    }
    public void removeCourseTime(int subjIdx, int courseIdx, Cord courseTime) {
        ArrayList<Cord> courseTimes = inputPlan.getSubjs().get(subjIdx).getCourses().get(courseIdx).getCourseTimes();
        Iterator<Cord> it = courseTimes.iterator();
        while(it.hasNext()) {
            Cord cord = it.next();
            if(cord.getDay() == courseTime.getDay() && cord.getTime() == courseTime.getTime()) {
                it.remove();
            }
        }
        mainController.updateCourseTableView();
    }

    /*launch algorithm*/
    public void launchAlgorithm() {
        _select = new boolean[inputPlan.MAX_TIME][inputPlan.MAX_DAY];
        for(int i = 0; i < inputPlan.MAX_TIME; i++) {
            for(int j = 0; j < inputPlan.MAX_DAY; j++) {
                _select[i][j] = false;
            }
        }
        outputPlan.clearAll();
        _courses = new LinkedList<>();
        _launchAlgorithm(0);
        setTableCnt(0);
        showResultTable();
    }
    private boolean[][] _select;
    LinkedList<OutputPlan.Table.Course> _courses;
    private void _launchAlgorithm(int depth) {
        if(depth >= inputPlan.getSubjs().size()) {
            outputPlan.getTables().add(new OutputPlan.Table(new LinkedList<OutputPlan.Table.Course>(_courses)));
            return;
        }
        InputPlan.Subj subj = inputPlan.getSubjs().get(depth);
        if(!subj.isEnabled()) {
            _launchAlgorithm(depth + 1);
            return;
        }
        Iterator<InputPlan.Subj.Course> it = subj.getCourses().iterator();
        while(it.hasNext()) {
            InputPlan.Subj.Course course = it.next();
            if(!course.isEnabled()) continue;
            ArrayList<Cord> cords = course.getCourseTimes();
            Iterator<Cord> it2 = cords.iterator();
            boolean flag = true;
            while(it2.hasNext()) {
                Cord cord = it2.next();
                if(_select[cord.getTime()][cord.getDay()]) {
                    flag = false;
                    break;
                }
            }
            if(!flag) continue;
            it2 = cords.iterator();
            while(it2.hasNext()) {
                Cord cord = it2.next();
                _select[cord.getTime()][cord.getDay()] = true;
            }
            _courses.offerLast(new OutputPlan.Table.Course(subj.getSubjName(), course.getProfessor(), new LinkedList<Cord>(cords)));
            _launchAlgorithm(depth + 1);
            it2 = course.getCourseTimes().iterator();
            while(it2.hasNext()) {
                Cord cord = it2.next();
                _select[cord.getTime()][cord.getDay()] = false;
            }
            _courses.pollLast();
        }
    }

    /*Bfmaker*/
    public Bfmaker() {
        newInputPlan(InputPlan.DEFAULT_MAX_DAY, InputPlan.DEFAULT_MAX_TIME);
        newOutputPlan(InputPlan.DEFAULT_MAX_DAY, InputPlan.DEFAULT_MAX_TIME);
        jsonMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InputPlan.class, new InputPlanDeserializer());
        jsonMapper.registerModule(module);
    }
    public Bfmaker(int MAX_DAY, int MAX_TIME) {
        newInputPlan(MAX_DAY, MAX_TIME);
        newOutputPlan(MAX_DAY, MAX_TIME);
        jsonMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InputPlan.class, new InputPlanDeserializer());
        jsonMapper.registerModule(module);
    }
    public Bfmaker(File file) {
        jsonMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InputPlan.class, new InputPlanDeserializer());
        jsonMapper.registerModule(module);
        openInputPlan(file);
        newOutputPlan(inputPlan.MAX_DAY, inputPlan.MAX_TIME);
    }
    public void startNewBfmaker(Bfmaker bfmaker) {
        new Runnable() {
            @Override
            public void run() {
                Stage stage = new Stage();
                bfmaker.start(stage);
            }
        }.run();
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            mainPane = loader.load();
            mainController = loader.getController();
            mainController.setBfmaker(this);
            primaryStage.setTitle("bfmaker");
            primaryStage.setScene(new Scene(mainPane));
            updateAll();
            primaryStage.show();
        } catch(IOException ioe) {}
    }
}
