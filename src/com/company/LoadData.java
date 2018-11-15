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

public class LoadData {
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

    File file;
    int m_size;
    int subProblem[][],backTrack[][];
    int[][] cities;
    int pow;
    public int cost=-1;
    public ArrayList<Integer> trip = new ArrayList<>();
    public void Load() throws FileNotFoundException {
        myTextArea.clear();
        trip.clear();
        String workingDir = System.getProperty("user.dir");
        workingDir += "\\" + "\\Files";
        JFileChooser jFileChooser = new JFileChooser(workingDir);
       if( jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
            file = new File(jFileChooser.getSelectedFile().toString());
            Scanner in = new Scanner(file);
        m_size=in.nextInt();
        cities = new int[m_size][m_size];
//Wczytanie danych do macierzy
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
        //Tworzę tablice podproblemow i tablicę do "nawracania"
        //pow: liczba 2 podniesiona do potegi rozmiaru macierzy
        //Jeżeli zbiór ma n elementów, to ilość wszystkich jego podzbiorów(stanów) wynosi n*2^n
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
    public void initDynamicProgrammingFor100Instances(){
        Random rand = new Random();
        long DPtime=0;
        long DPavgTime=0;
        m_size =Integer.parseInt(dp_instanceTextField.getText());
        for(int h=0;h<1;h++){
            cities = new int[m_size][m_size];
            for(int i=0;i<m_size;i++){
                for(int j=0;j<m_size;j++){
                    cities[i][j]=rand.nextInt(50)+1;
                    if(i==j){
                        cities[i][j]=-1;
                    } //System.out.print(cities[i][j]+" ");
                }
                //System.out.println("\n");
            }
           // System.out.println("#####################\n");
            pow=(int)Math.pow(2,m_size);
            subProblem = new int[m_size][pow];
            backTrack = new int[m_size][pow];

            for(int i = 0;i<m_size;i++){
                for (int j = 0;j<pow;j++){
                    subProblem[i][j]= -1;
                    backTrack[i][j]= -1;
                }
            }
//            System.out.println("wypisuje");
//            for(int i=0 ;i<m_size;i++){
//                for(int j=0;j<pow;j++){
//                   System.out.print(subProblem[i][j]+"  ");
//                }
//                System.out.println();
//            }

            for (int i=0;i<m_size;i++){
                subProblem[i][0]=cities[i][0];
            }
            long startTimeDP = System.nanoTime();

            cost = DynamicProgramming(0,pow-2);

            long stopTimeDP = System.nanoTime();
            DPtime=(stopTimeDP-startTimeDP);
            //System.out.println(String.valueOf(DPtime)+"\n");
            DPavgTime+=DPtime;
        }
        myTextArea.appendText("\nCzas średni dla macierzy N metodą Programowania Dynamicznego= "+m_size+": "+DPavgTime/100+" nanosekund\n");
    }
    public void initDynamicProgramming(){
        for (int i=0;i<m_size;i++){
            subProblem[i][0]=cities[i][0];
        }
        myTextArea.appendText("\n");
        myTextArea.appendText("Tablica podproblemów: \n");
        for (int i = 0; i < m_size; i++) {
            for (int j = 0; j < pow; j++) {
                myTextArea.appendText(subProblem[i][j]+"   ");
            } myTextArea.appendText("\n");
        }
        myTextArea.appendText("\n");
        trip.add(1); //Daje poczatkowe miasto
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
               mask=pow-1-(int)Math.pow(2,i); //Maska do okreslania podzbioru POPRAWIC ################################################
                subSetting = setup & mask;
               myTextArea.appendText("\nUstawiam na: "+setup+"    Maska: "+ mask+"    subUstawienie: "+ subSetting+ "    Miasto= "+(i+1));
                if (subSetting != setup) {

                    //myTextArea.appendText( "\nWartość do policzenia: "+"miasto: od ["+(startingPoint+1)+"] do ["+(i+1)+"] = "+cities[startingPoint][i] + " + reszta drogi ("+i+","+subSetting+")\n");
                    theValue = cities[startingPoint][i] + DynamicProgramming(i, subSetting);
                   // myTextArea.appendText("Wartość policzona: miasto: od ["+(startingPoint+1)+"] do ["+(i+1)+"] = "+cities[startingPoint][i]+" + reszta drogi ("+i+","+subSetting+") = " +DynamicProgramming(i,subSetting)+ " = "+theValue+"\n");

                    if (result == -1 || theValue < result) {
                        result = theValue;
                        backTrack[startingPoint][setup] = i;
                       // myTextArea.appendText("Tablica powrotów ["+startingPoint+"]["+setup+"]= "+ backTrack[startingPoint][setup]+"\n");
                    }
                }else { //myTextArea.appendText(("--> omijam"));
                }
            }

            subProblem[startingPoint][setup] = result;
            //myTextArea.appendText("\nPodproblem ["+startingPoint+"]["+setup+"]= "+subProblem[startingPoint][setup]+"\n");
           // myTextArea.appendText("\n");
            //myTextArea.appendText("Tablica podproblemów: \n");
           // for (int i = 0; i < m_size; i++) {
               // for (int j = 0; j < pow; j++) {
                    //myTextArea.appendText(subProblem[i][j]+"   ");
               // } //myTextArea.appendText("\n");
            //}
            //myTextArea.appendText("\n");
           //System.out.println("Wynik = "+result+"\n");
            return result;
        }


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
        myTextArea.appendText("Tablica podproblemów: \n");
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
        myTextArea.appendText("Tablica do backtrackingu: \n");
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
        myTextArea.appendText("Najkrotsza możliwa droga: ");
        for(int i=0;i<trip.size();i++){
            if(i+1!=trip.size())
                myTextArea.appendText(trip.get(i)+"-->");

            else
                myTextArea.appendText(trip.get(i)+"");
        }
        myTextArea.appendText("\nKoszt drogi: "+cost+"\n");
        myTextArea.appendText("\n");
    }

    public void  printCities(){
        myTextArea.appendText("\n");
        myTextArea.appendText("Macierz "+m_size+"x"+m_size+"\n");
        for(int i=0;i<m_size;i++){
            for (int j=0;j<m_size;j++){
                myTextArea.appendText(cities[i][j]+"\t");
            }
            myTextArea.appendText("\n");
        }
        myTextArea.appendText("\n");
    }

    int shortestDistance;
    String shortestRoute;
    int g=0;

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
                } //System.out.print(cities[i][j]+" ");
            }//System.out.print("\n");
        }
        long startTimeBF = System.nanoTime();
        populate("", m_size);
        long stopTimeBF = System.nanoTime();
        BFTime = stopTimeBF - startTimeBF;
        //myTextArea.appendText("["+g+"]= "+String.valueOf(BFTime)+"\n");
        BFavgTime += BFTime;
    }myTextArea.appendText("\nCzas średni dla macierzy N metodą Brute-Force= "+m_size+": "+BFavgTime/100+" nanosekund\n");
}
    int gn=0;
int cn=0;
    protected void populate(String sign, int k)
    {
        //System.out.print("K["+g+"] "+k+"\n");
        g++;
        cn++;
//        if (cn == 9000000) //licznik jesli algorytm dlugo trwa ot pokazuje ze sie nic nie zacielo
//        {
//            gn++;
//            System.out.println("["+gn+"] .");
//            cn = 0;
//
//        }
        if(k == 0)
        {
            //System.out.print("sign:"+sign+"\n");
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
            //System.out.print("k: "+k+"   ");
            //System.out.print("cs : "+cs+"   ");
            //System.out.print("S: "+sign+"\n");
            CharSequence cr;
            if(!sign.contains(cs))
            {
                cr = ""+cs;
               // System.out.print("cr: "+cr+"\n");
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
            //System.out.println("Route["+i+"]: "+route[i]+"  ");
        }
        //System.out.print("route lenght: "+route.length+"\n");
        for(int i = 0; i<route.length-1; i++)
        { //System.out.print("Route["+i+"]: "+route[i]+"\n");
            route[i] = route[i+1];
          // System.out.print("Route["+(i+1)+"]: "+route[i]+"\n");
        }
        route[route.length-1] = route[0];
        for(int i=0;i<route.length;i++){
            //System.out.print("Route["+i+"]: "+route[i]+"  ");
        }
        System.out.println("\n");
        int distance = 0;
        for(int i = 0; i<route.length-1; i++)
        {
            distance += cities[Integer.parseInt(route[i])][Integer.parseInt(route[i+1])];

            //System.out.print("distMatrix["+i+"]["+(i+1)+"]= "+cities[Integer.parseInt(route[i])][Integer.parseInt(route[i+1])]+"\n");
            //System.out.print("distance: "+distance+"\n");
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
       myTextArea.appendText("Koszt drogi: " + shortestDistance + "\n" + "Najkrotsza możliwa droga: " + s+"-1");
    }

}
