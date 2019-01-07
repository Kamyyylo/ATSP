package com.company;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GeneticAlgorithmController {

    public LoadFile lf = new LoadFile();
    @FXML
    private Pane geneticAlgorithmPane;
    @FXML
    private Button chooseFileButton;

    @FXML
    private RadioButton transpositionRadioButton;

    @FXML
    private ToggleGroup mutationType;

    @FXML
    private RadioButton insertionRadioButton;
    @FXML
    private TextArea geneticAlgorithmTextArea;

    @FXML
    private Button closeButton;

    @FXML
    private TextField workingTimeTextField;

    @FXML
    private TextField crossoverTextField;

    @FXML
    private TextField mutationTextField;

    @FXML
    private TextField populationSizeTextField;
    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void loadFile() throws FileNotFoundException {
        lf.loadFile();
    }
    public void startAlgorithm()
    {
        //for(int i=0;i<10;i++) {
            int mutationType = 0;
            GeneticAlgorithm g = new GeneticAlgorithm(lf);
            int populationSize = Integer.parseInt(populationSizeTextField.getText());
            int workTime = Integer.parseInt(workingTimeTextField.getText());
            int childrenPairsSize = 50;
            float mutationProbability = Float.parseFloat(mutationTextField.getText());
            float crossProbability = Float.parseFloat(crossoverTextField.getText());
            if (insertionRadioButton.isSelected()) mutationType = 1;
            if (transpositionRadioButton.isSelected()) mutationType = 2;
            int r = g.solution(populationSize, workTime, childrenPairsSize, mutationProbability, crossProbability, mutationType);
            geneticAlgorithmTextArea.appendText(g.printResults());
       // }

    }
    @FXML
    private void printCities()
    {
        geneticAlgorithmTextArea.appendText(lf.printCities()+"");
    }
    public void previousPane()throws IOException {
        Pane dpandbfPane = FXMLLoader.load(getClass().getResource("DPandBFPane.fxml"));
        geneticAlgorithmPane.getChildren().setAll(dpandbfPane);



    }
}
