package com.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.Scanner;
public class Main extends Application {
    public static void main(String[] args){Application.launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane TSPPane = FXMLLoader.load(getClass().getResource("TSPApp.fxml"));
        primaryStage.setScene(new Scene(TSPPane));
        primaryStage.setTitle("Asymmetric Traveling Salesman Problem");
        primaryStage.show();
    }
}
