package com.company;
import java.util.ArrayList;
import java.util.Comparator;
public class Nodes implements Cloneable{                                  //implements cloneable: ability to clone objects
    public int border =9999999;
    public ArrayList<Integer> cityPath;;                                 //path with cities to count
    public int pathCost;                                                     //cost of the whole path
    public int value;                                                    //value to copy(need to be 9999999 - cost)
    Nodes(ArrayList<Integer> cityPath, LoadFile lf){                    //constructor that takes path and our loaded cities
        this.cityPath = cityPath;
        pathCost=lf.pathCostList(cityPath);                              //method that counts cost of the cities
        value = border -pathCost;
    }
    public static Nodes createNewInstance(Nodes newNodes, LoadFile lf){         //method that create new instance of the Node
        Nodes nodes = new Nodes(newNodes.cityPath,lf);                         //create new class object
        nodes.cityPath = newNodes.cityPath;
        nodes.pathCost=lf.pathCostList(newNodes.cityPath);
        nodes.value =nodes.border -nodes.pathCost;
        return nodes;
    }
    public static int copyCity(ArrayList<Integer> Path, int i ){          //method that copy the city
        int a = Path.get(i);
        return a;
    }
    public static int copyValue (Nodes n){                               //method that copy the value
        int a = n.border -n.pathCost;
        return a;
    }
    public void countValue (LoadFile s){                                 //count value of the path

        pathCost=s.pathCostList(cityPath);
        value= border -pathCost;
    }
    public int getValue(){                                              //get value to copy
        return value;
    }
    public static Comparator<Nodes> ValueComparator = (s1, s2) -> {      //comparing two objects in ascending position
        int v1 = s1.getValue();
        int v2 = s2.getValue();
        return v1-v2;
    };
    public String printPath(){                                              //printing path
        String path ="";
        for(int i = 0; i< cityPath.size(); i++){
            path+= cityPath.get(i)+ " ";
        }
        path+="  Path cost: "+pathCost+" \n";
        return path;
    }
}