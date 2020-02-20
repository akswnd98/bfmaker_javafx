package bfmaker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class NewSubjController {
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

    public void setNecessary(Bfmaker bfmaker, Stage dialogStage) {
        setBfmaker(bfmaker);
        setDialogStage(dialogStage);
    }

    @FXML private TextField subjNameTextField;

    private void ok() {
        if(subjNameTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setHeaderText("input error");
            alert.setContentText("empty");
            alert.showAndWait();
        } else {
            bfmaker.newSubj(subjNameTextField.getText());
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
