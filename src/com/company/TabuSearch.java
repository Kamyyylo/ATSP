package com.company;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class TabuSearch {

        public int [][] tabuList;                                           //tabu list
        public ArrayList<Integer> Path = new ArrayList<>();                 //cityPath with cities to shuffle and swaping
        public ArrayList<Integer> theBestSolutionPath = new ArrayList<>();   //The best cityPath og cities
        public ArrayList<String> printPathList  = new ArrayList<>();
        public int time;                                                    //time to tabuList
        int m_size;
        public int critical=0;                                              //Counter to critical moment in tabuSearch
        int maxCritical;                                                    //Maximum of critical repeats
        int theBestSolutionCost=999999;
        long timeCounterStart, timeOfFoundTheBestPath,FinalTime;
        long timeCounterStop=0;
        LoadFile lf;
    TabuSearch(LoadFile lf){
        this.lf=lf;
        m_size=lf.m_size;
    }
        public void startAlgorithm( int workTime, int instances, int neighborhood) {

            int algorithmWorkTime=workTime*60000;
            int neighbor= neighborhood;
            System.out.println("Rozmiar macierzy: " + m_size);
            tabuList = new int[m_size][m_size];
        for(int w=0;w<instances;w++) {
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

            Solution(algorithmWorkTime,neighbor, m_size);
            printPathList();
        }
        }
    public void Solution(int algorithmWorkTime, int neighbor, int m_size){
        java.util.Collections.shuffle(Path);                                //Permutation of cities list
        System.out.print("shuffled list: "+ Path+"\n");
        ArrayList<Integer>temporaryNewSolution;
        theBestSolutionCost =lf.pathCostList(Path);                                   //assigned first list as the best solution
        ArrayList<Integer> BestSolution= new ArrayList<>();                     //List of best solution
        timeCounterStart=System.currentTimeMillis();
       while(timeCounterStop-timeCounterStart<=algorithmWorkTime)                                              //Main loop
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
                        if(neighbor==1)
                        swap(temporaryNewSolution,j,k);                                       //swaping neighbors
                        if(neighbor==2)
                        insert(temporaryNewSolution,j,k);                                     //inserting neighbors
                        if(neighbor==3)
                        invert(temporaryNewSolution,j,k);                                     //inverting neighbors
                        int newCost=lf.pathCostList(temporaryNewSolution);                     //Counting cost of trip for generated list of cities

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
                decrementation(m_size);                                              //decrementation tabuList for future possible usage pare of cities
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
            if(neighbor==1)
                swap(Path,city_1,city_2);                                         //swap for city 1 and city 2 which are cities with better cost
            if(neighbor==2)
                insert(Path,city_1,city_2);                                     //insert for city 1 and city 2 which are cities with better cost
            if(neighbor==3)
                invert(Path,city_1,city_2);                                     //invert for city 1 and city 2 which are cities with better cost
            criticalEvent(bestCost,theBestSolutionPath,m_size);                               //Checking critival events, diversification
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

    public void criticalEvent(int bestCost,ArrayList<Integer> bestBestSol, int m_size){
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
                System.out.println("cityPath cost list(path) "+lf.pathCostList(Path));
                System.out.println("cityPath cost list(tmp) "+lf.pathCostList(tmp));
                if(lf.pathCostList(Path)>lf.pathCostList(tmp)){                      //if temporary list found better result
                    Path= new ArrayList<>();                                   //create new path list with temporary path steps
                    for(int n=0;n<Path.size();n++){
                        Path.add(tmp.get(n));
                    }
                    if(theBestSolutionCost>lf.pathCostList(Path)){                       //The same situtation with the best cost. if found better path --> replace
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

    public void decrementation(int m_size){                                               //decrementation of tabu after every loop step
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

    public void makePathList(){                                                    //printing final path with the best result
      printPathList.add("\nThe best cost --> "+ lf.pathCostList(theBestSolutionPath)+"\t\t\nThe best path found --> ");

        for(int i=0;i<theBestSolutionPath.size();i++){
            printPathList.add(theBestSolutionPath.get(i)+" ");
        }
        printPathList.add(""+theBestSolutionPath.get(0));
        printPathList.add("\n");
        printPathList.add("When the best path was found: "+ FinalTime);

        theBestSolutionPath.clear();
        Path.clear();

    }
    public ArrayList printPathList()
    {
        return printPathList;
    }



}
