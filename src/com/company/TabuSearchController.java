package com.company;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TabuSearchController {
    LoadFile lf = new LoadFile();

    @FXML
    private Pane tabuSearchPane;

    @FXML
    private TextArea tabuSearchTextArea;

    @FXML
    private TextField algorithmWorkTimeTextField;

    @FXML
    private RadioButton insertRadioButton;

    @FXML
    private ToggleGroup Neighborhood;

    @FXML
    private RadioButton swapRadioButton;

    @FXML
    private RadioButton invertRadioButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField instancesTextField;

    public void loadFile() throws FileNotFoundException {
        lf.loadFile();
    }
    @FXML
    private void printCities()
    {
        tabuSearchTextArea.appendText(lf.printCities()+"");
    }
    public void startAlgorithm() {
        TabuSearch ts = new TabuSearch(lf);
        int neighborhood = 0;
        int workTime =Integer.parseInt(algorithmWorkTimeTextField.getText());
        int instances = Integer.parseInt(instancesTextField.getText());
        if (swapRadioButton.isSelected()) neighborhood=1;
        if (insertRadioButton.isSelected()) neighborhood=2;
        if (invertRadioButton.isSelected()) neighborhood=3;
        ts.startAlgorithm(workTime,instances,neighborhood);
        ts.makePathList();
        tabuSearchTextArea.appendText(ts.printPathList()+"");
    }

    public void previousPane()throws IOException {
        Pane dpandbfPane = FXMLLoader.load(getClass().getResource("DPandBFPane.fxml"));
        tabuSearchPane.getChildren().setAll(dpandbfPane);
    }

    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
