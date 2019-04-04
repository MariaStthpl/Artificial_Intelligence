package ai;

import javafx.util.Pair;
import java.util.*;

public class AStar {
	
    //Priority List (or priority queue): objects are ordered by their priority
    //minimum estimated cost to goalNode


    public static LinkedList constructPath(AStarNode node){
        LinkedList path = new LinkedList();
        while (node.pathParent != null){
            path.addFirst(node);
            node = node.pathParent;
        }
        return path;
    }


    public static LinkedList<AStarNode> findPath(AStarNode startNode, AStarNode goalNode, HashMap<Pair<Double, Double>, ArrayList<Integer>> Junctions, HashMap<Integer, Road> Roads){
        PriorityQueue<AStarNode> openList = new PriorityQueue<>((x,y) -> (x.getf()<y.getf())?-1:1);
        LinkedList<AStarNode> closedList = new LinkedList();

        startNode.g = 0.0;
        startNode.h = startNode.getEstimatedCost(goalNode);
        startNode.pathParent = null;
        openList.add(startNode);

        while(!openList.isEmpty()){
            AStarNode node = openList.poll();
            if ((node.x == goalNode.x)&&(node.y == goalNode.y)){
                //construct the path from start to goal
                return constructPath(node);
            }

            ArrayList<AStarNode> neighbors = node.getNeighbors(Junctions, Roads);
            assert neighbors!=null;
            for (AStarNode neighborNode : neighbors) {

                //boolean isOpen = openList.contains(neighborNode);
                boolean isOpen = false;
                for (AStarNode temp : openList){
                    if ((temp.x == neighborNode.x) && (temp.y == neighborNode.y)){
                        isOpen = true;
                        break;
                    }
                }

                //boolean isClosed = closedList.contains(neighborNode);
                boolean isClosed = false;
                for (AStarNode temp : closedList){
                    if ((temp.x == neighborNode.x) && (temp.y == neighborNode.y)){
                        isClosed = true;
                        break;
                    }
                }

                double g = node.g + node.getEstimatedCost(neighborNode);

                //check if the neighbor node has not been traversed or
                // if a shorter path to this neighbor node is found
                if ((!isOpen && !isClosed) || g < neighborNode.g) {
                    neighborNode.pathParent = node;
                    neighborNode.g = g;
                    neighborNode.h = neighborNode.getEstimatedCost(goalNode);
                    if (isClosed)
                        closedList.remove(neighborNode);
                    if (!isOpen)
                        openList.add(neighborNode);
                }
            }
            closedList.add(node);
        }

        //no path found
        return null;
	
    }
    
}
