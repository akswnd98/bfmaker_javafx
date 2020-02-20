package bfmaker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class NewCourseController {
    /*dialogStage*/
    private Stage dialogStage;
    public Stage getDialogStage() {
        return dialogStage;
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /*bfmaker*/
    private Bfmaker bfmaker;
    public Bfmaker getBfmaker() {
        return bfmaker;
    }
    public void setBfmaker(Bfmaker bfmaker) {
        this.bfmaker = bfmaker;
    }

    /*mainController*/
    private MainController mainController;
    public MainController getMainController() {
        return mainController;
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /*call after fxml load*/
    public void setNecessary(Bfmaker bfmaker, Stage dialogStage) {
        setBfmaker(bfmaker);
        setDialogStage(dialogStage);
        setMainController(bfmaker.getMainController());
    }

    @FXML private TextField professorTextField;

    private void ok() {
        if(professorTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setHeaderText("input error");
            alert.setContentText("empty");
            alert.showAndWait();
        } else {
            int subjIdx = mainController.getSubjListView().getSelectionModel().getSelectedIndex();
            bfmaker.newCourse(subjIdx, professorTextField.getText());
            dialogStage.close();
        }
    }

    @FXML private void onOkAction(ActionEvent event) {
        ok();
    }
    @FXML private void onCancelAction(ActionEvent event) {
        dialogStage.close();
    }
    @FXML private void onKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            ok();
        }
    }
}
