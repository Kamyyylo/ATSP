package com.company;
import java.util.*;
public class GeneticAlgorithm {

    public int theBestResult;
    public Nodes bestNode;                                              
    public int amountOfTheCities;                                                           //number of cities
    public ArrayList<Integer> cityList;                                                     //list with cities (f.e 0,1,2,3,4,5 if amount of cities==6)
    public ArrayList<Nodes> population;                                                     //list of all our nodes
    public ArrayList<Nodes> parentsPopulation= new ArrayList<>();
    public ArrayList<Nodes> childrenPopulation= new ArrayList<>();
    long timeCounterStart, timeCounterStop, timeFoundBest;
    LoadFile lf;
    GeneticAlgorithm(LoadFile lf){                                                          //contructor with loaded file with cities as a parameter
        this.lf=lf;
        amountOfTheCities =lf.m_size;
        population=new ArrayList<>();
        cityList = new ArrayList<>();
        for(int i = 0; i< amountOfTheCities; i++){                                          //filing the list with cities
            cityList.add(i);
        }
        bestNode =new Nodes(cityList,lf);
    }
    int solution(int populationSize,int workingTime, int childrenPairsSize,float mutationProbability,float crossProbability, int mutationType){
        int actualBestResult;                                                               //actual the best result during this loop
        int algorithmWorkTime=workingTime*60000;                                            //time to count when algorithm has to stop
        createFirstPopulation(populationSize);                                              //starting population
        theBestResult =population.get(0).value;                                             //starting best result
        bestNode =population.get(0);                                                        //starting population best node (list of cities)
        int amountOfParents =populationSize/2;
        timeCounterStart=System.currentTimeMillis();                                        //Start Time
        while(timeCounterStop-timeCounterStart<=algorithmWorkTime)                          //we put the end time to the algorithm
        {
            sortingPopulation(population);                                                     //sorting population from the best to the worst cost path
            parentsPopulation=findParentsInPopulation(populationSize,amountOfParents);      //parent population making (populationSize/2)
            for(int j=0; j<childrenPairsSize;j++){
                ArrayList<Nodes> childrenPair =generateChidrenPair(mutationProbability,crossProbability,mutationType);   //generating children pairs with mutation and cross probability
                childrenPopulation.add(childrenPair.get(0));                                                //add new children to the population
                childrenPopulation.add(childrenPair.get(1));
            }
            sortingPopulation(childrenPopulation);                                             //soritng children population
            population= populationSelection(populationSize);                                //choosing new population
            childrenPopulation = new ArrayList<>();
            actualBestResult=Nodes.copyValue(population.get(0));                            //copy the best value
            if(actualBestResult> theBestResult){                                            //if found better result --> replace
                theBestResult =Nodes.copyValue(population.get(0));
                bestNode =Nodes.createNewInstance(population.get(0),lf);
                timeFoundBest=System.currentTimeMillis();
            }
            timeCounterStop=System.currentTimeMillis();

        }

        return 9999999-theBestResult;
    }

    public String printResults()
    {
        String text;
        text =("\nThe best solution: " + (9999999 - theBestResult) + "\n");
        text= text+("Time when best result found: " + ((timeFoundBest-timeCounterStart)/1000)+" sec\n");

        //Collections.sort(bestNode.cityPath);
        text = text+(bestNode.cityPath);
        return text;
    }
    void createFirstPopulation(int populationSize){                                                  //creating first population
        for(int i=0;i<populationSize;i++){
            ArrayList<Integer> singlePopulationNode=(ArrayList<Integer>) cityList.clone();       //duplicate the object
            java.util.Collections.shuffle(singlePopulationNode);                                 //shuffling list of cities
            Nodes n = new Nodes(singlePopulationNode,lf);                                        //adding new created node
            population.add(n);                                                                    //adding node to population
        }
    }

    // tworzenie pary nowych dzieci
    ArrayList<Nodes> generateChidrenPair(float mutationProbability,float crossP, int mutationType){
        ArrayList<Nodes> parentsPair = generateParentsPair(crossP);                     //generate parents for children
        ArrayList<Nodes> childrenPair= crossParent(parentsPair);                        //crossing parents to make children
        childrenPair.get(0).cityPath = attemptChildMutation(mutationProbability, childrenPair.get(0),mutationType);
        childrenPair.get(1).cityPath = attemptChildMutation(mutationProbability, childrenPair.get(1),mutationType);
        childrenPair.get(0).countValue(lf);                                             //counting value of mutated children
        childrenPair.get(1).countValue(lf);
        return childrenPair;
    }

    //mutacja
    ArrayList<Integer> attemptChildMutation(float mutationProbability,Nodes n, int mutationType){            //creating mutated childrens
        Random rand = new Random();
        float r = rand.nextFloat();                                                        //number from 0.0 to 1.0
        if(r<=mutationProbability){                                                   //if mutation probability is bigger than randomly generated number
            int firsElementIndex;
            int secondElementIndex;
            do{
                firsElementIndex=rand.nextInt(n.cityPath.size());                       //generate random number from 0 to size of the city path
                secondElementIndex=rand.nextInt(n.cityPath.size());
            } while(firsElementIndex==secondElementIndex && (secondElementIndex-firsElementIndex<=(n.cityPath.size()*0.4f)));   //we dont want to mutate elements in very big area
            if(mutationType==1)return insertion(n,firsElementIndex);
            if(mutationType==2)return transposition(n,firsElementIndex,secondElementIndex);
        }
        else{
            return n.cityPath;
        }
        return null;
    }
    // wybranie pary rodzicielskiej z populacji rodzicielskiej
    ArrayList<Nodes> generateParentsPair (float crossP){                            //Method to generate parents
        ArrayList<Nodes> parentsPair = new ArrayList<>();                           //list with nodes that keeps mother and father
        int fatherIndex;
        int motherIndex;
        Random rand = new Random();
        float r ;
        do {
            fatherIndex=0;
            motherIndex=0;
            r=rand.nextFloat();                                                     //Random number from 0.0 to 1.0
            while (fatherIndex == motherIndex) {
                fatherIndex = rand.nextInt(parentsPopulation.size());               //Generating random index for mother and father
                motherIndex = rand.nextInt(parentsPopulation.size());               //from 0 to parentPopulation.size-->(population/2)
            }
        }while (crossP<r);      //thanks to this while, we can use out probability of the crossover occurence
        parentsPair.add(Nodes.createNewInstance(parentsPopulation.get(fatherIndex),lf));        //here i add to the parentPair list new instance of parents for children
        parentsPair.add(Nodes.createNewInstance(parentsPopulation.get(motherIndex),lf));
        return parentsPair;
    }
    ArrayList<Nodes> findParentsInPopulation(int populationSize,int parentPopulationSize){       //method that will find random parents
        ArrayList<Nodes> selectedParents = new ArrayList<>();                                   //list with selected parents to work
        long permutationsEstimatedSum =0;
        for(int i=0;i<populationSize;i++){                                                       //loop sum all costs in this population
            permutationsEstimatedSum+=population.get(i).value;
        }
        long randomTargetValue;
        sortingPopulation(population);                                                             //sorting population again
        for(int i=0;i<parentPopulationSize;i++){
            randomTargetValue= generateRandomValue(permutationsEstimatedSum);                     //generate random values
            long currentValue=0;
            long previousValue=0;
            for(int j=0;j<populationSize;j++){
                currentValue+=population.get(j).value;
                if((previousValue<=randomTargetValue)&&(randomTargetValue<=currentValue)){
                    selectedParents.add(Nodes.createNewInstance(population.get(i),lf));
                    break;
                }
                else{
                    previousValue=previousValue+population.get(j).value;
                }
            }
        }
        return selectedParents;

    }
    ArrayList<Nodes> crossParent(ArrayList<Nodes> parentsPair){                         //crossingover PMX
        int sizeOfTheChildren = parentsPair.get(0).cityPath.size();                     //taking number of cities
        int crossPointOne=0;
        int crossPointTwo=0;                                                         //cross point to do the cross
        ArrayList<Integer>child_1 = new ArrayList<>();
        ArrayList<Integer>child_2=new ArrayList<>();
        for(int i=0;i<sizeOfTheChildren;i++){                                       //filing child lists with -1 to fill the whole size
            child_1.add(-1);
            child_2.add(-1);
        }
        Random rand = new Random();
        while(crossPointOne==crossPointTwo){
            crossPointOne=rand.nextInt(sizeOfTheChildren);                          //lottery of cross points in city list(from 0 to city list size)
            crossPointTwo=rand.nextInt(sizeOfTheChildren);
        }
        if(crossPointOne>crossPointTwo){                                            //if the first point is bigger than first --> swap points
            int tmp = crossPointTwo;
            crossPointTwo=crossPointOne;
            crossPointOne=tmp;
        }
        for(int i =crossPointOne;i<crossPointTwo;i++){

            child_1.set(i,Nodes.copyCity(parentsPair.get(1).cityPath,i));           //first child gets some mother genes
            child_2.set(i,Nodes.copyCity(parentsPair.get(0).cityPath,i));           //second child gets some father genes
        }
        for(int i=0;i<sizeOfTheChildren;i++)                                        //filing the genes that are not in collision with the previous one
        {
            for(int j =crossPointOne;j<crossPointTwo;j++) {
                if (!child_1.contains(parentsPair.get(0).cityPath.get(i)))          //if child_1 doesn't contains element --> add
                {
                    child_1.set(i,parentsPair.get(0).cityPath.get(i));              //setting element on free place
                }
            }
            for(int j =crossPointOne;j<crossPointTwo;j++) {
                if (!child_2.contains(parentsPair.get(1).cityPath.get(i)))
                {
                    child_2.set(i,parentsPair.get(1).cityPath.get(i));

                }
            }
        }
        ArrayList<Integer> helpList_1 = new ArrayList<>();
        ArrayList<Integer> citiesThatAreNotInList_1 = new ArrayList<>();
        int iterator_1=0;
        for(int h=0; h<sizeOfTheChildren;h++)
        {
            helpList_1.add(h);                                                      //adding list of cities to list
        }
        for(int h=0; h<sizeOfTheChildren;h++)
        {
            if(!child_1.contains(helpList_1.get(h)))                                //if child doesn't have this city-->
           {
               citiesThatAreNotInList_1.add(helpList_1.get(h));                     //add to the second help list

           }
        }
        for(int i=0;i<sizeOfTheChildren;i++)                                        //adding rest of the cities
        {
            if(child_1.get(i)==-1){
                child_1.set(i,citiesThatAreNotInList_1.get(iterator_1));
                iterator_1++;
            }
        }

        ArrayList<Integer> helpList_2 = new ArrayList<>();
        ArrayList<Integer> citiesThatAreNotInList_2 = new ArrayList<>();
        int iterator_2=0;
        for(int h=0; h<sizeOfTheChildren;h++)
        {
            helpList_2.add(h);
        }
        for(int h=0; h<sizeOfTheChildren;h++)
        {
            if(!child_2.contains(helpList_2.get(h)))
            {
                citiesThatAreNotInList_2.add(helpList_2.get(h));

            }
        }

        for(int i=0;i<sizeOfTheChildren;i++)
        {

            if(child_2.get(i)==-1){
                child_2.set(i,citiesThatAreNotInList_2.get(iterator_2));
                iterator_2++;
            }

        }
        ArrayList<Nodes> newNodeList = new ArrayList<>();                               //adding new nodes to the list
        Nodes newNode = new Nodes(child_1,lf);
        newNodeList.add(Nodes.createNewInstance(newNode,lf));
        newNode = new Nodes(child_2,lf);
        newNodeList.add(Nodes.createNewInstance(newNode,lf));
        return newNodeList;
    }

    long generateRandomValue(long permutationsEvaluationSum){                   //Random target value generator
        Random rand = new Random();                                             //New Random
        float random = rand.nextFloat();                                        //Random value from 0.0 to 1.0
        float target=random*(permutationsEvaluationSum);
        return (long)target;
    }
    ArrayList<Nodes> populationSelection(int populationSize){                                //choosing new population from children population and previous population
        ArrayList<Nodes> newPopulation = new ArrayList<>();
        ArrayList<Nodes> currentPopulation;
        ArrayList<Nodes> currentPopulationChildren;
        currentPopulation=(ArrayList<Nodes>)population.clone();                                 //cloning actual population
        currentPopulationChildren =(ArrayList<Nodes>)childrenPopulation.clone();                 //cloning children population
        for(int i=0; i<populationSize;i++){
            if(currentPopulationChildren.isEmpty() && !currentPopulation.isEmpty()){             //if there is no children but the population exist
                newPopulation.add(Nodes.createNewInstance(currentPopulation.get(0),lf));         //adding population and removing first element
                currentPopulation.remove(0);
                continue;
            }
            if(!currentPopulationChildren.isEmpty() && currentPopulation.isEmpty()){             //if there are childrens and the population is empty
                newPopulation.add(Nodes.createNewInstance(currentPopulationChildren.get(0),lf)); //adding children population
                currentPopulation.remove(0);
                continue;
            }
            if(currentPopulationChildren.get(0).pathCost<currentPopulation.get(0).pathCost){    //if the children cost is lower than current population cost
                newPopulation.add(Nodes.createNewInstance(currentPopulationChildren.get(0),lf));
                currentPopulationChildren.remove(0);
            }
            else{
                newPopulation.add(Nodes.createNewInstance(currentPopulation.get(0),lf));
                currentPopulation.remove(0);
            }
        }
        sortingPopulation(newPopulation);
        return newPopulation;
    }

    void sortingPopulation(ArrayList<Nodes> population){                                   //sort the population from the best to the worst

        for(int i=0;i<population.size();i++){                                           //counting value of the path for all population
            population.get(i).countValue(lf);
        }
        Collections.sort(population,Nodes.ValueComparator);                             //sorting population ascending
        Collections.reverse(population);                                                //reverse the population

    }
    void printPopulation(ArrayList<Nodes> population){
        for(int i=0;i<population.size();i++){
            System.out.print(i +") "+population.get(i).printPath());
        }
    }
    public ArrayList<Integer> transposition (Nodes n, int i ,int j){                    //mutation



        int tmp = n.cityPath.get(i);                    //value of i
        n.cityPath.set(i,n.cityPath.get(j));            //on i place set j element
        n.cityPath.set(j,tmp);          //on j place set i element
        n.countValue(lf);

        return n.cityPath;
}
    public ArrayList<Integer> insertion(Nodes n, int i)
    {   System.out.println("Droga przed mutacja: "+ n.cityPath);
        int value = n.cityPath.get(i);
        Random rand = new Random();
        int random = rand.nextInt(n.cityPath.size());
        n.cityPath.remove(i);
        n.cityPath.add(random,value);
        System.out.println("Droga po mutacji: "+ n.cityPath);
        return n.cityPath;
    }
}
