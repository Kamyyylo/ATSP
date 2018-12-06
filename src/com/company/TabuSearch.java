package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.swing.*;
public class TabuSearch {
    @FXML
    private Pane tabuSearchPane;
    @FXML
    private TextArea tabuSearchTextArea;
    @FXML
    private TextField algorithmWorkTimeTextField;
    @FXML
    private RadioButton insertRadioButton;

    @FXML
    private RadioButton swapRadioButton;

    @FXML
    private RadioButton invertRadioButton;
    @FXML
    private TextField instancesTextField;
    @FXML
    private Button closeButton;
    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
        public ArrayList<String> matrixToShow =new ArrayList<>();           //Showing matrix
        public int m_size = -1;
        public int[][] cities;
        public int cost=0;
        public File file;
        public int [][] tabuList;                                           //tabu list
        public ArrayList<Integer> Path = new ArrayList<>();                 //Path with cities to shuffle and swaping
        public ArrayList<Integer> theBestSolutionPath = new ArrayList<>();   //The best Path og cities
        public int time;                                                    //time to tabuList
        public int critical=0;                                              //Counter to critical moment in tabuSearch
        int maxCritical;                                                    //Maximum of critical repeats
        int theBestSolutionCost=999999;
        long timeCounterStart, timeOfFoundTheBestPath,FinalTime;
        long timeCounterStop=0;
        public void loadFile ()throws FileNotFoundException {

            String workingDir = System.getProperty("user.dir");
            workingDir += "\\" + "\\Files";                                 //Place where files to load exist
            JFileChooser jFileChooser = new JFileChooser(workingDir);
            if( jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
            {
            file = new File(jFileChooser.getSelectedFile().toString());
            Scanner in = new Scanner(file);
            matrixToShow.clear();
            for(int i=0;i<8;i++) {                                          //Loop to decode .atsp file and it's content
                if(i==1){
                    matrixToShow.add(in.next());
                    if(in.next().equals("ATSP")){
                        matrixToShow.add("ATSP\n");
                    }
                    else {                                                  //if file is not .ATSP
                        JOptionPane.showMessageDialog(null,"Choose .atsp file!");
                        jFileChooser = new JFileChooser(workingDir);
                        if( jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
                            file = new File(jFileChooser.getSelectedFile().toString());
                           in = new Scanner(file);
                           matrixToShow.clear();
                           i =0;
                    }
                    matrixToShow.add(in.nextLine());
                }
                else if(i==3){
                    matrixToShow.add(in.next());
                    m_size=in.nextInt();
                    matrixToShow.add(String.valueOf(m_size));
                }
                else {
                    matrixToShow.add(in.nextLine()+"\n");
                }
            }
            cities= new int [m_size][m_size];                                  //matrix with cities
                for (int i = 0; i < m_size; i++) {
                    for (int j = 0; j < m_size; j++) {
                        cities[i][j] = in.nextInt();
                        if (i == j) {
                            cities[i][j] = -1;
                        }
                    }
                }

        }
        addCitiesToList();
        }
        public void addCitiesToList(){                                          //adding cities to list that will be show in textarea
            for(int i=0;i<m_size;i++){
                for (int j=0;j<m_size;j++){
                    matrixToShow.add(cities[i][j]+"\t");
                }
                matrixToShow.add("\n");
            }
        }
    public void  printCities(){                                                 //Printing matrix
            tabuSearchTextArea.clear();
        tabuSearchTextArea.appendText(matrixToShow + "");
    }
        public int pathCostList(ArrayList<Integer> Path){
            cost =0;
            for(int i=0;i<Path.size()-1;i++){
                cost=cost+cities[Path.get(i)][Path.get(i+1)];
            }

            cost=cost+cities[Path.get(Path.size()-1)][Path.get(0)];
            return cost;
        }

        public void startAlgorithm() {
            System.out.println("Rozmiar macierzy: " + m_size);
            tabuList = new int[m_size][m_size];
        for(int w=0;w<Integer.parseInt(instancesTextField.getText());w++) {
            for (int i = 0; i < m_size; i++) {                                  //create path list with size of number of cities
                Path.add(i);

                for (int j = 0; j < m_size; j++) {                              //fill tabuList with zeros
                    tabuList[i][j] = 0;
                }
            }
            System.out.print(Path);
            time = m_size;                                                  //Time for cities staying in tabuList

            maxCritical = m_size * 2;
            System.out.println("\nMaxCritical: " + maxCritical);

            Solution();
            printPath();


        }
        }
    public void Solution(){
        java.util.Collections.shuffle(Path);                                //Permutation of cities list
        System.out.print("shuffled list: "+ Path+"\n");
        ArrayList<Integer>temporaryNewSolution;
        theBestSolutionCost =pathCostList(Path);                                   //assigned first list as the best solution
        ArrayList<Integer> BestSolution= new ArrayList<>();                     //List of best solution
        timeCounterStart=System.currentTimeMillis();
       while(timeCounterStop-timeCounterStart<=(Integer.parseInt(algorithmWorkTimeTextField.getText())*60000))                                              //Main loop
        {

            int city_1=0;                                                   //cities that will be put in tabuList
            int city_2=0;
            int bestCost=999999;
            for(int j=1;j<m_size;j++){                                      //First city randomly taken is start city that's why I start from one
                for(int k=2;k<m_size;k++){                                  //Second city in queue
                    if(j!=k){
                        temporaryNewSolution= new ArrayList<>();                          //Make new list with solution
                        for(int m=0;m<Path.size();m++){
                            temporaryNewSolution.add(Path.get(m));

                        }
                        if(swapRadioButton.isSelected())
                        swap(temporaryNewSolution,j,k);                                       //swaping neighbors
                        if(insertRadioButton.isSelected())
                        insert(temporaryNewSolution,j,k);                                     //inserting neighbors
                        if(invertRadioButton.isSelected())
                        invert(temporaryNewSolution,j,k);                                     //inverting neighbors
                        int newCost=pathCostList(temporaryNewSolution);                     //Counting cost of trip for generated list of cities

                        if(newCost<bestCost && tabuList[j][k]==0){            //if counted value is better and tabuList allow --> go to the loop
                            BestSolution= new ArrayList<>();                       //new best solution
                            for(int m=0;m<Path.size();m++){
                                BestSolution.add(temporaryNewSolution.get(m));
                            }
                            city_1=j;                                         //This cities will be assigned to tabuList
                            city_2=k;
                            bestCost=newCost;
                        }
                    }
                }
            }
            if(city_1 !=0){
                decrementation();                                              //decrementation tabuList for future possible usage pare of cities
                addTabu(city_1,city_2);                                        //Adding tabu to cities
            }
            if(theBestSolutionCost>bestCost){                                         //saving the best of the best result
                timeOfFoundTheBestPath=System.currentTimeMillis();
                theBestSolutionCost=bestCost;
                theBestSolutionPath= new ArrayList<>();
                for(int m=0;m<BestSolution.size();m++){
                    theBestSolutionPath.add(BestSolution.get(m));                           //List with the best path
                }
               FinalTime=timeOfFoundTheBestPath-timeCounterStart;
            }
            if(swapRadioButton.isSelected())
                swap(Path,city_1,city_2);                                         //swap for city 1 and city 2 which are cities with better cost
            if(insertRadioButton.isSelected())
                insert(Path,city_1,city_2);                                     //insert for city 1 and city 2 which are cities with better cost
            if(invertRadioButton.isSelected())
                invert(Path,city_1,city_2);                                     //invert for city 1 and city 2 which are cities with better cost
            criticalEvent(bestCost,theBestSolutionPath);                               //Checking critival events, diversification
            System.out.print("\t best cost: ");
            printBestCost();
            timeCounterStop=System.currentTimeMillis();
            System.out.println("Algorithm time stand: "+ (timeCounterStop-timeCounterStart)+"ms");

        }
    }

    public void swap (ArrayList<Integer>Path, int i ,int j){
        int tmp = Path.get(i);
        Path.set(i,Path.get(j));
        Path.set(j,tmp);

    }
    public void insert(ArrayList<Integer>Path, int i ,int j){
        int pos = Path.indexOf(Path.get(i));
        int pos2=Path.indexOf(Path.get(j));
        int temporary = Path.get(j);
        Path.remove(pos2);
        Path.add(pos,temporary);

    }

    public void invert(ArrayList<Integer>Path, int i ,int j){
            ArrayList<Integer> temporary_1 = new ArrayList<>();
            ArrayList<Integer> temporaryReverse=new ArrayList<>();
            int pos1 = Path.indexOf(Path.get(i));
            int pos2=Path.indexOf(Path.get(j));
            if(i<j){
                for(int h=0; h<pos1;h++){
                    temporary_1.add(Path.get(h));
                }
                for(int h=pos1;h<pos2;h++){
                    temporaryReverse.add(Path.get(h));
                }Collections.reverse(temporaryReverse);
                for(int h=0; h<temporaryReverse.size();h++){
                    temporary_1.add(temporaryReverse.get(h));
                }
                for(int h=pos2; h<Path.size();h++){
                    temporary_1.add(Path.get(h));
                }
            }
        if(i>j){
            for(int h=0; h<pos2;h++){
                temporary_1.add(Path.get(h));
            }
            for(int h=pos2;h<pos1;h++){
                temporaryReverse.add(Path.get(h));
            }Collections.reverse(temporaryReverse);
            for(int h=0; h<temporaryReverse.size();h++){
                temporary_1.add(temporaryReverse.get(h));
            }
            for(int h=pos1; h<Path.size();h++){
                temporary_1.add(Path.get(h));
            }
        }

        Path.clear();
        for(int h=0;h<temporary_1.size();h++)
        {
        Path.add(temporary_1.get(h));
        }
    }

    public void criticalEvent(int bestCost,ArrayList<Integer> bestBestSol){
        if(theBestSolutionCost<bestCost){                                              //if result is not better critical++
            critical++;
            System.out.println("Critical: "+critical);
        }
        else{
            critical=0;

        }

        if(critical>maxCritical){                                              //if critical exceeded maxCritical value
            ArrayList<Integer> tmp = new ArrayList<>();                        //create temporary list
            for(int n=0;n<Path.size();n++){                                    //rewrite path to temporary list
                tmp.add(Path.get(n));
            }
            for(int m=0;m<m_size;m++){
                java.util.Collections.shuffle(tmp);                            //shuffle temporary list
                System.out.println("Path cost list(path) "+pathCostList(Path));
                System.out.println("Path cost list(tmp) "+pathCostList(tmp));
                if(pathCostList(Path)>pathCostList(tmp)){                      //if temporary list found better result
                    Path= new ArrayList<>();                                   //create new path list with temporary path steps
                    for(int n=0;n<Path.size();n++){
                        Path.add(tmp.get(n));
                    }
                    if(theBestSolutionCost>pathCostList(Path)){                       //The same situtation with the best cost. if found better path --> replace
                        bestBestSol= new ArrayList<>();
                        for(int n=0;n<Path.size();n++){
                            bestBestSol.add(Path.get(n));
                        }
                    }
                }
            }
            for(int m=0;m<m_size;m++){                                         //clearing tabuList for new path
                for(int n=0;n<m_size;n++){
                    tabuList[m][n]=0;
                }
            }
            critical=0;
        }
    }


    public void addTabu(int city1,int city2){
        tabuList[city1][city2]+= time;                                          //adding tabu for analogous cities
        tabuList[city2][city1]+= time;
    }

    public void decrementation(){                                               //decrementation of tabu after every loop step
        for(int i=0;i<m_size;i++){
            for(int j=0;j<m_size;j++){
                if(tabuList[i][j]>0){
                    tabuList[i][j]--;
                }
            }
        }

    }

    public void printBestCost(){
        System.out.println(theBestSolutionCost);                                       //printing path with the best cost
    }

    public void printPath(){                                                    //printing final path with the best result
       tabuSearchTextArea.appendText("\nThe best cost --> "+ pathCostList(theBestSolutionPath)+"\t\t\nThe best path found --> ");

        for(int i=0;i<theBestSolutionPath.size();i++){
            tabuSearchTextArea.appendText(theBestSolutionPath.get(i)+" ");
        }
        tabuSearchTextArea.appendText(""+theBestSolutionPath.get(0));
        tabuSearchTextArea.appendText("\n");
        tabuSearchTextArea.appendText("When the best path was found: "+ FinalTime);

        theBestSolutionPath.clear();
        Path.clear();

    }

    public void previousPane()throws IOException {
        Pane dpandbfPane = FXMLLoader.load(getClass().getResource("DPandBFPane.fxml"));
        tabuSearchPane.getChildren().setAll(dpandbfPane);



    }


}
