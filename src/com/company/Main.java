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
        primaryStage.setTitle("TSP");
        primaryStage.show();
    }

}
//public class Main  {
//
//    public static void main(String[] args) throws FileNotFoundException{
//
//        LoadData loadData = new LoadData();
//        int choose=9999;
//        while(choose!=3){
//    System.out.println("Problem Komiwojazera");
//    System.out.println("   Kamil Kamyszek\n       2018");
//    System.out.println("       Menu \n--> 1)Wczytaj dane \n--> 2)Wyswietl dane \n--> 3)Wyjscie\n--> Wybor: ");
//        Scanner scanner = new Scanner(System.in);
//        choose=scanner.nextInt();
//
//switch(choose){
//    case 1:
//        loadData.Load();
//        break;
//    case 2:
//        loadData.showMatrix();
//        break;
//    case 3 :
//        System.exit(0);
//}
//        }
//    }
//
//}
