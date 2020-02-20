package bfmaker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewController {
    private Bfmaker bfmaker;
    private Stage dialogStage;
    public void setNecessary(Bfmaker bfmaker, Stage dialogStage) {
        this.bfmaker = bfmaker;
        this.dialogStage = dialogStage;
    }
    @FXML private TextField maxDayTextField;
    @FXML private TextField maxTimeTextField;
    @FXML private void onOkAction(ActionEvent event) {
        String maxDayString = maxDayTextField.getText();
        String maxTimeString = maxTimeTextField.getText();
        boolean flag = true;
        for(int i = 0; i < maxDayString.length(); i++) {
            if(!Character.isDigit(maxDayString.charAt(i))) {
                flag = false;
                break;
            }
        }
        for(int i = 0; i < maxTimeString.length(); i++) {
            if(!Character.isDigit(maxTimeString.charAt((i)))) {
                flag = false;
                break;
            }
        }
        if(maxDayString.isEmpty() || maxTimeString.isEmpty()) {
            flag = false;
        }
        if(flag) {
            int maxDay = Integer.parseInt(maxDayString);
            int maxTime = Integer.parseInt(maxTimeString);
            Bfmaker newBfmaker = new Bfmaker(maxDay, maxTime);
            new Runnable() {
                @Override
                public void run() {
                    newBfmaker.startNewBfmaker(newBfmaker);
                }
            }.run();
            dialogStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setHeaderText("input error");
            alert.setContentText("empty or not a number");
            alert.showAndWait();
        }
    }
    @FXML private void onCancelAction(ActionEvent event) {
        dialogStage.close();
    }
}
