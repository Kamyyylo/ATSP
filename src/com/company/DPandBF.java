package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.swing.*;

public class DPandBF {
    @FXML
    private ChoiceBox<String> fileChoiceBox;

    @FXML
    private Pane TSPPane;

    @FXML
    private TextField matrixSizeTextField;

    @FXML
    private TextField fileNameTextField;

    @FXML
    private Button chooseButton;

    @FXML
    private TextArea myTextArea;

    @FXML
    private TextField dp_instanceTextField;
    @FXML
    private TextField bf_instanceTextField;
    @FXML
    private Button fileChooseTextField;

    File file;                                                      // File to read
    int m_size;                                                     //size of matrix
    int subProblem[][],backTrack[][];                               //subproblem and backtrack tables
    int[][] cities;                                                 //tables with cities cost
    int pow;                                                        //help attribute
    public int cost=-1;                                             //default cost
    public ArrayList<Integer> trip = new ArrayList<>();             //list with the shortest road
    public ArrayList<String> stepsDP = new ArrayList<>();           // list with steps of DP algorithm
    public ArrayList<String> stepsBF = new ArrayList<>();           //list with steps of BF algorithm
    public void Load() throws FileNotFoundException {
        myTextArea.clear();
        trip.clear();
        String workingDir = System.getProperty("user.dir");
        workingDir += "\\" + "\\Files";                             //Place where files to load exist
        JFileChooser jFileChooser = new JFileChooser(workingDir);
       if( jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
            file = new File(jFileChooser.getSelectedFile().toString());
            Scanner in = new Scanner(file);
        m_size=in.nextInt();                                        //Size is first int in file
        cities = new int[m_size][m_size];
                                                                    //loading data to matrix:
        for(int i=0;i<m_size;i++){
            for(int j=0;j<m_size;j++){
                cities[i][j]=in.nextInt();
                if(i==j){
                    cities[i][j]=-1;
                }
            }
        }
       in.close();
       }
        //Create subproblem and backtrack table
        //pow: number 2 up to power of N
        //If matrix has N elements, the number of his supbrolem is n*2^n
       pow=(int)Math.pow(2,m_size);
        subProblem = new int[m_size][pow];
        backTrack = new int[m_size][pow];
        for(int i = 0;i<m_size;i++){
            for (int j = 0;j<pow;j++){
                subProblem[i][j]= -1;
                backTrack[i][j]= -1;
            }
        }

    }

    public int getM_size() {
        return m_size;
    }

    public void setM_size(int m_size) {
        this.m_size = m_size;
    }
    public void initDynamicProgrammingFor100Instances(){
        Random rand = new Random();
        long DPtime=0;
        long DPavgTime=0;
        m_size =Integer.parseInt(dp_instanceTextField.getText());
        for(int h=0;h<1;h++){
            cities = new int[m_size][m_size];
            for(int i=0;i<m_size;i++){
                for(int j=0;j<m_size;j++){
                    cities[i][j]=rand.nextInt(50)+1;            //random from 0 to 50
                    if(i==j){
                        cities[i][j]=-1;
                    }
                }
            }
            pow=(int)Math.pow(2,m_size);
            subProblem = new int[m_size][pow];
            backTrack = new int[m_size][pow];

            for(int i = 0;i<m_size;i++){
                for (int j = 0;j<pow;j++){
                    subProblem[i][j]= -1;                               //filing tables with -1
                    backTrack[i][j]= -1;
                }
            }
            for (int i=0;i<m_size;i++){
                subProblem[i][0]=cities[i][0];
            }
            long startTimeDP = System.nanoTime();
            cost = DynamicProgramming(0,pow-2);
            long stopTimeDP = System.nanoTime();
            DPtime=(stopTimeDP-startTimeDP);
            DPavgTime+=DPtime;
        }
        myTextArea.appendText("\nAverage Time for matrix = "+m_size+" for 100 instances using dynamic programming is : "+DPavgTime/100+" nanoseconds\n");
    }
    public void initDynamicProgramming(){
        for (int i=0;i<m_size;i++) {
            subProblem[i][0] = cities[i][0];
        }
        trip.add(1);
        cost = DynamicProgramming(0,pow-2);
        getTrip(0,pow-2);
        trip.add(1);
    }
    public int DynamicProgramming(int startingPoint,int setup){
        int subSetting=0, mask=0,result=-1,theValue;

        if(subProblem[startingPoint][setup]!=-1){
            return subProblem[startingPoint][setup];
        }
        else {

            for (int i = 0; i < m_size; i++) {
               mask=pow-1-(int)Math.pow(2,i);                           //setting mask
                subSetting = setup & mask;                              //subsetting to recurency
                stepsDP.add("\nSetup: "+setup+"    Mask: "+ mask+"    subSetting: "+ subSetting+ "    City= "+(i+1));
                if (subSetting != setup) {

                    stepsDP.add( "\nValue to count: "+"city: from ["+(startingPoint+1)+"] to ["+(i+1)+"] = "+cities[startingPoint][i] + " + rest of the road ("+i+","+subSetting+")\n");
                    theValue = cities[startingPoint][i] + DynamicProgramming(i, subSetting);
                    stepsDP.add("\nValue counted: city: from ["+(startingPoint+1)+"] to ["+(i+1)+"] = "+cities[startingPoint][i]+" + rest of the road ("+i+","+subSetting+") = " +DynamicProgramming(i,subSetting)+ " = "+theValue+"\n");

                    if (result == -1 || theValue < result) {
                        result = theValue;
                        backTrack[startingPoint][setup] = i;
                        stepsDP.add("Backtracking["+startingPoint+"]["+setup+"]= "+ backTrack[startingPoint][setup]+"\n");
                    }
                }else { stepsDP.add(("--> Skip this city"));
                }
            }
            subProblem[startingPoint][setup] = result;
            stepsDP.add("\nsubproblem ["+startingPoint+"]["+setup+"]= "+subProblem[startingPoint][setup]+"\n");
            stepsDP.add("SubProblem table: \n");
            for (int i = 0; i < m_size; i++) {
                for (int j = 0; j < pow; j++) {
                    stepsDP.add(subProblem[i][j]+"   ");
                } stepsDP.add("\n");
            }
            stepsDP.add("\n");
            stepsDP.add("Result = "+result+"\n");
            return result;
        }
    }
    public void getStepsDP(){
        myTextArea.appendText(stepsDP+"");
    }
    public void getTrip(int startingPoint, int setup){
        if(backTrack[startingPoint][setup]==-1){
            return;
        }
        int x= backTrack[startingPoint][setup];
        int mask = pow -1-(int)Math.pow(2,x);
        int masked = setup & mask;
        trip.add(x+1);//+1 bo liczymy od 0
        getTrip(x,masked);
    }
    public void printSubProblem(){
        myTextArea.appendText("\n");
        myTextArea.appendText("SubProblem table: \n");
        for(int i=0 ;i<m_size;i++){
            for(int j=0;j<pow;j++){
                myTextArea.appendText(subProblem[i][j]+"  ");
            }
            myTextArea.appendText("\n");
        }
        myTextArea.appendText("\n");
    }
    public void printBackTracking(){
        myTextArea.appendText("\n");
        myTextArea.appendText("BackTrack table: \n");
        for(int i=0 ;i<m_size;i++){
            for(int j=0;j<pow;j++){
                myTextArea.appendText(backTrack[i][j]+" ");
            }
            myTextArea.appendText("\n");
        }
        myTextArea.appendText("\n");
    }

    public void printResult(){
        myTextArea.appendText("\n");
        myTextArea.appendText("Shortest possible way: ");
        for(int i=0;i<trip.size();i++){
            if(i+1!=trip.size())
                myTextArea.appendText(trip.get(i)+"-->");

            else
                myTextArea.appendText(trip.get(i)+"");
        }
        myTextArea.appendText("\nTrip cost: "+cost+"\n");
        myTextArea.appendText("\n");
    }

    public void  printCities(){
        myTextArea.appendText("\n");
        myTextArea.appendText("Matrix "+m_size+"x"+m_size+"\n");
        for(int i=0;i<m_size;i++){
            for (int j=0;j<m_size;j++){
                myTextArea.appendText(cities[i][j]+"\t");
            }
            myTextArea.appendText("\n");
        }
        myTextArea.appendText("\n");
    }
    //############################################################################################################
    //Brute Force Method
    int shortestDistance;
    String shortestRoute;
    int k =0;
    int gn=0;
    int counter =0;
    public void initBruteForce(){
        populate("",m_size);
    }

    public void initBruteForceFor100Instances(){
        long BFTime=0;
        long BFavgTime=0;
        Random rand1 = new Random();
        m_size=Integer.parseInt(bf_instanceTextField.getText());
        cities = new int[m_size][m_size];
        for(int g=0;g<100;g++) {
            for (int i = 0; i < m_size; i++) {
                for (int j = 0; j < m_size; j++) {
                    cities[i][j] = rand1.nextInt(50) + 1;
                    if (i == j) {
                        cities[i][j] = -1;
                    }
                }
            }
            long startTimeBF = System.nanoTime();
            populate("", m_size);
            long stopTimeBF = System.nanoTime();
            BFTime = stopTimeBF - startTimeBF;
           stepsBF.add("\n["+g+"]= "+String.valueOf(BFTime)+"\n");
            BFavgTime += BFTime;
        }myTextArea.appendText("\nAverage Time for matrix = "+m_size+" for 100 instances using brute force is : "+BFavgTime/100+" nanoseconds\n");
    }

    protected void populate(String sign, int k)
    {

        stepsBF.add("\nK["+ this.k +"] "+k+"\n");
        this.k++;
        counter++;
        if (counter == 9000000)                                         //Counter to show that the program is running
        {
            gn++;
            myTextArea.appendText("["+gn+"]in progress");
            counter = 0;

        }
        if(k == 0)
        {
            stepsBF.add("sign:"+sign+"\n");
            int ds = getDistance(sign);
            if(shortestDistance == 0 || shortestDistance > ds)
            {
                shortestDistance = ds;
                shortestRoute = "" + sign;
            }

        }

        for(int i = 0; i<m_size; i++)
        {
            CharSequence cs = ""+i;
            stepsBF.add("k: "+k+"   ");
            stepsBF.add("cs : "+cs+"   ");
            stepsBF.add("S: "+sign+"\n");
            CharSequence cr;
            if(!sign.contains(cs))
            {
                cr = ""+cs;
                stepsBF.add("cr: "+cr+"\n");
            }
            else
            {
                continue;
            }
            if(k >= 1)
            {
                populate(sign + "-" + cr, k-1);
            }
        }
    }

    private int getDistance(String s)
    {
        String[] route = s.split("-");
        for(int i=0;i<route.length;i++){
            stepsBF.add("Route["+i+"]: "+route[i]+"  \n");
        }
        stepsBF.add("route lenght: "+route.length+"\n");
        for(int i = 0; i<route.length-1; i++)
        {
            route[i] = route[i+1];
            stepsBF.add("Route["+(i+1)+"]: "+route[i]+"");
        }
        stepsBF.add("\n");
        route[route.length-1] = route[0];
        for(int i=0;i<route.length;i++){
            stepsBF.add("Route["+i+"]: "+route[i]+"  ");
        }
        stepsBF.add("\n");
        int distance = 0;
        for(int i = 0; i<route.length-1; i++)
        {
            distance += cities[Integer.parseInt(route[i])][Integer.parseInt(route[i+1])];

            stepsBF.add("\ndistMatrix["+i+"]["+(i+1)+"]= "+cities[Integer.parseInt(route[i])][Integer.parseInt(route[i+1])]+"\n");
            stepsBF.add("distance: "+distance+"\n");
        }
        return distance;
    }
    public void bruteForcePrintResult(){
        String s = "";
        for(int i = 1; i< shortestRoute.length(); i++)
        {
            if(shortestRoute.charAt(i) != '-')
                s += Character.getNumericValue(shortestRoute.charAt((i))) + 1;
            else
                s += shortestRoute.charAt(i);
        }
        myTextArea.appendText("\n");
        myTextArea.appendText("Trip cost: " + shortestDistance + "\n" + "Shortest possible way: " + s+"-1\n");
    }
    public void getStepsBF()
    {
        myTextArea.appendText(stepsBF+"");
    }
}
