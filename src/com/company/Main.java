package com.company;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DPandBFPane.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();

    }


    public static void main(String[] args) throws FileNotFoundException {
        launch(args);
//        int populationSize =1000;
//        int itr =2000;
//        int childrenPairsSize =500;//populationSize/2;
//        float mutationProbability =0.08f;
//        float crossProbability = 0.8f;
//        LoadFile lf = new LoadFile();
//        lf.loadFile();
//        GeneticAlgorithm g = new GeneticAlgorithm(lf);
//        //g.createStartingPopulation(5);
//        //g.printPopulation();
//        int r = g.solution(populationSize, itr, childrenPairsSize, mutationProbability, crossProbability);
//        System.out.println(9999999-r);
    }



}