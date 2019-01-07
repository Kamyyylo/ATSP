package com.company;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadFile {
    public ArrayList<String> matrixToShow = new ArrayList<>();
    public File file;
    public int m_size;
    public int[][] cities;
    public int cost;


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
    public ArrayList printCities(){                                                 //Printing matrix
        return matrixToShow;
    }

    public int pathCostList(ArrayList<Integer> Path){
        cost =0;
        for(int i=0;i<Path.size()-1;i++){
            cost=cost+cities[Path.get(i)][Path.get(i+1)];
        }
        cost=cost+cities[Path.get(Path.size()-1)][Path.get(0)];

        return cost;
    }
}
