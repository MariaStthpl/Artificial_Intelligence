package ai;

import javafx.util.Pair;
import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class AStarNode implements Comparable<AStarNode> {

    public AStarNode pathParent;
    public double g;
    public double h;

    public double x;
    public double y;

    public int id;

    /*AStarNode(double x, double y, int id){
        this.x = x;
        this.y = y;
        this.id = id;
    }
    */
    public double getf(){
        return this.g+this.h;
    }

    @Override
    public int compareTo(AStarNode object) {
        return (object.getf() < this.getf())?-1:1;
    }

    //euclidean
    public double getEstimatedCost(AStarNode goalNode){
        return sqrt(pow((this.x - goalNode.x),2) + pow(this.y - goalNode.y,2));
    }

    public ArrayList<AStarNode> getNeighbors(HashMap<Pair<Double, Double>, ArrayList<Integer>> Junctions, HashMap<Integer, Road> Roads){
    	
        ArrayList<AStarNode> neighbors = new ArrayList<>();

        double[] coord = new double[2];
        coord[0] = this.x;
        coord[1] = this.y;

        Pair<Double, Double> pair = new Pair<>(coord[0], coord[1]);

        Road currentRoad = Roads.get(this.id);
        int i = 0;
        while (i<currentRoad.coordinates.size()){
            if ((currentRoad.coordinates.get(i)[0] == coord[0])&&(currentRoad.coordinates.get(i)[1] == coord[1]))
                break;
            i++;
        }
        AStarNode neighbor = new AStarNode();
        if ((i == 0) && currentRoad.coordinates.size()>1){
            neighbor.x = currentRoad.coordinates.get(i+1)[0];
            neighbor.y = currentRoad.coordinates.get(i+1)[1];
            neighbor.id = this.id;
            neighbors.add(neighbor);
        } else if (i == currentRoad.coordinates.size()-1){
            neighbor.x = currentRoad.coordinates.get(i-1)[0];
            neighbor.y = currentRoad.coordinates.get(i-1)[1];
            neighbor.id = this.id;
            neighbors.add(neighbor);
        } else {
            neighbor.x = currentRoad.coordinates.get(i+1)[0];
            neighbor.y = currentRoad.coordinates.get(i+1)[1];
            neighbor.id = this.id;
            neighbors.add(neighbor);
            neighbor = new AStarNode();
            neighbor.x = currentRoad.coordinates.get(i-1)[0];
            neighbor.y = currentRoad.coordinates.get(i-1)[1];
            neighbor.id = this.id;
            neighbors.add(neighbor);
        }
        AStarNode neighborJ;
        if (Junctions.containsKey(pair)){
            ArrayList<Integer> currentJunction = Junctions.get(pair);
            i = 0;
            while(i<currentJunction.size()){
                if (currentJunction.get(i) != id){
                    neighborJ = new AStarNode();
                    Road checkRoad = Roads.get(currentJunction.get(i));
                    int j = 0;
                    while (j<checkRoad.coordinates.size()){
                        if ((checkRoad.coordinates.get(j)[0] == coord[0])&& (checkRoad.coordinates.get(j)[1] == coord[1]))
                            break;
                        j++;
                    }
                    if (j == 0){
                        neighborJ.x = checkRoad.coordinates.get(j+1)[0];
                        neighborJ.y = checkRoad.coordinates.get(j+1)[1];
                        neighborJ.id = currentJunction.get(i);
                        neighbors.add(neighborJ);
                    } else if (j == checkRoad.coordinates.size()-1){
                        neighborJ.x = checkRoad.coordinates.get(j-1)[0];
                        neighborJ.y = checkRoad.coordinates.get(j-1)[1];
                        neighborJ.id = currentJunction.get(i);
                        neighbors.add(neighborJ);
                    } else {
                        neighborJ.x = checkRoad.coordinates.get(j+1)[0];
                        neighborJ.y = checkRoad.coordinates.get(j+1)[1];
                        neighborJ.id = currentJunction.get(i);
                        neighbors.add(neighborJ);
                        neighborJ = new AStarNode();
                        neighborJ.x = checkRoad.coordinates.get(j-1)[0];
                        neighborJ.y = checkRoad.coordinates.get(j-1)[1];
                        neighborJ.id = currentJunction.get(i);
                        neighbors.add(neighborJ);
                    }
                }
                i++;
            }
        }


    return (neighbors);
    }
}
